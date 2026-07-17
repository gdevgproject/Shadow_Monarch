package dev.umbra.core.impl.combat;

import dev.umbra.UmbraMod;
import dev.umbra.core.contract.combat.CombatService;
import dev.umbra.core.contract.combat.DodgeDirection;
import dev.umbra.core.contract.combat.UmbraCombatStatePayload;
import dev.umbra.core.contract.combat.UmbraDodgeStatePayload;
import dev.umbra.core.contract.state.StateSaveService;
import dev.umbra.core.contract.state.UmbraPlayerState;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Random;

public final class CombatServiceImpl implements CombatService {
    private static final int COMBO_DECAY_TICKS = 60; // 3 seconds
    private static final int STANCE_DECAY_TICKS = 200; // 10 seconds
    private static final int FOCUS_RECOVERY_LOCK_TICKS = 7;
    private static final int PRECISION_DODGE_TICKS = 2;
    private static final int PRECISION_MANA_COOLDOWN_TICKS = 20;
    private static final int RESOURCE_SYNC_INTERVAL_TICKS = 10;
    private static final double DODGE_FOCUS_COST = 25.0;
    private static final int DODGE_FATIGUE_COST = 1;
    private static final double[] DODGE_VELOCITY_CURVE = {0.78, 0.67, 0.55, 0.45, 0.34, 0.25, 0.18};

    private final Map<UUID, PlayerCombatState> playerStates = new HashMap<>();
    private final Random random = new Random();

    private StateSaveService getStateSaveService() {
        return UmbraMod.getServiceRegistry()
            .locate(StateSaveService.class)
            .orElseThrow(() -> new IllegalStateException("StateSaveService not registered"));
    }

    private static class PlayerCombatState {
        boolean inCombatStance = false;
        int comboCount = 0;
        long lastAttackTick = 0;
        long lastDamageTick = 0;
        long lastCombatActionTick = 0;
        long dodgeStartTick = Long.MIN_VALUE;
        long dodgeActionEndTick = Long.MIN_VALUE;
        long dodgeIFrameEndTick = Long.MIN_VALUE;
        long focusRecoveryLockedUntilTick = 0;
        long lastPrecisionManaRewardTick = -PRECISION_MANA_COOLDOWN_TICKS;
        long lastResourceSyncTick = 0;
        DodgeDirection dodgeDirection;
        DodgeDirection pendingDodgeDirection;
        long pendingDodgeExpireTick = Long.MIN_VALUE;
    }

    private PlayerCombatState getOrCreateCombatState(UUID uuid, long currentTick) {
        return playerStates.computeIfAbsent(uuid, k -> {
            PlayerCombatState state = new PlayerCombatState();
            state.lastAttackTick = currentTick;
            state.lastDamageTick = currentTick;
            state.lastCombatActionTick = currentTick;
            state.lastResourceSyncTick = currentTick - RESOURCE_SYNC_INTERVAL_TICKS;
            return state;
        });
    }

    @Override
    public boolean isInCombatStance(UUID playerUuid) {
        PlayerCombatState state = playerStates.get(playerUuid);
        return state != null && state.inCombatStance;
    }

    @Override
    public int getComboCount(UUID playerUuid) {
        PlayerCombatState state = playerStates.get(playerUuid);
        return state == null ? 0 : state.comboCount;
    }

    @Override
    public void enterCombatStance(ServerPlayer player) {
        UUID uuid = player.getUUID();
        long tick = ((net.minecraft.server.level.ServerLevel) player.level()).getServer() != null ? ((net.minecraft.server.level.ServerLevel) player.level()).getServer().getTickCount() : 0;
        PlayerCombatState state = getOrCreateCombatState(uuid, tick);
        state.lastCombatActionTick = tick;
        if (!state.inCombatStance) {
            state.inCombatStance = true;
            syncCombatState(player, state);
        }
    }

    @Override
    public void exitCombatStance(ServerPlayer player) {
        UUID uuid = player.getUUID();
        PlayerCombatState state = playerStates.get(uuid);
        if (state != null && state.inCombatStance) {
            state.inCombatStance = false;
            state.comboCount = 0;
            syncCombatState(player, state);
        }
    }

    @Override
    public void registerAttack(ServerPlayer player, LivingEntity target) {
        UUID uuid = player.getUUID();
        long tick = ((net.minecraft.server.level.ServerLevel) player.level()).getServer() != null ? ((net.minecraft.server.level.ServerLevel) player.level()).getServer().getTickCount() : 0;
        PlayerCombatState state = getOrCreateCombatState(uuid, tick);

        state.lastAttackTick = tick;
        state.lastCombatActionTick = tick;
        boolean changed = false;
        if (!state.inCombatStance) {
            state.inCombatStance = true;
            changed = true;
        }

        state.comboCount++;
        changed = true;

        if (changed) {
            syncCombatState(player, state);
        }
    }

    @Override
    public void registerDamage(ServerPlayer player) {
        UUID uuid = player.getUUID();
        long tick = ((net.minecraft.server.level.ServerLevel) player.level()).getServer() != null ? ((net.minecraft.server.level.ServerLevel) player.level()).getServer().getTickCount() : 0;
        PlayerCombatState state = getOrCreateCombatState(uuid, tick);

        state.lastDamageTick = tick;
        state.lastCombatActionTick = tick;
        if (!state.inCombatStance) {
            state.inCombatStance = true;
            syncCombatState(player, state);
        }
    }

    @Override
    public boolean requestDodge(ServerPlayer player, DodgeDirection direction) {
        if (player == null || direction == null || player.isSpectator() || player.isPassenger() || player.getAbilities().flying) {
            return false;
        }

        long tick = ((net.minecraft.server.level.ServerLevel) player.level()).getServer().getTickCount();
        PlayerCombatState combatState = getOrCreateCombatState(player.getUUID(), tick);
        UmbraPlayerState state = getStateSaveService().getOrCreatePlayerState(player.getUUID());
        if (tick < combatState.dodgeActionEndTick) {
            if (combatState.dodgeActionEndTick - tick <= 5) {
                combatState.pendingDodgeDirection = direction;
                combatState.pendingDodgeExpireTick = tick + 5;
                syncDodgeState(player, state, combatState, false);
                return true;
            }
            return false;
        }

        return startDodge(player, direction, tick, combatState, state);
    }

    private boolean startDodge(
        ServerPlayer player,
        DodgeDirection direction,
        long tick,
        PlayerCombatState combatState,
        UmbraPlayerState state
    ) {
        if (state.getFatigue() >= 100) {
            return false;
        }

        double focusCost = calculateDodgeFocusCost(state.getFatigue());
        if (state.getCurrentFocus() < focusCost) {
            syncDodgeState(player, state, combatState, false);
            return false;
        }

        // Dodge is the self-enable route into combat stance and never waits for attack recovery.
        combatState.inCombatStance = true;
        combatState.lastCombatActionTick = tick;
        state.setCurrentFocus(state.getCurrentFocus() - focusCost);
        state.setFatigue(Math.min(100, state.getFatigue() + DODGE_FATIGUE_COST));
        combatState.dodgeStartTick = tick;
        combatState.dodgeActionEndTick = tick + calculateDodgeActionTicks();
        combatState.dodgeIFrameEndTick = tick + calculateDodgeIFrameTicksForAgility(state.getAgility());
        combatState.focusRecoveryLockedUntilTick = tick + FOCUS_RECOVERY_LOCK_TICKS;
        combatState.dodgeDirection = direction;
        syncCombatState(player, combatState);
        syncDodgeState(player, state, combatState, false);
        return true;
    }

    @Override
    public boolean tryAbsorbDodgeDamage(ServerPlayer player) {
        if (player == null) {
            return false;
        }
        long tick = ((net.minecraft.server.level.ServerLevel) player.level()).getServer().getTickCount();
        PlayerCombatState combatState = playerStates.get(player.getUUID());
        if (combatState == null || tick < combatState.dodgeStartTick || tick >= combatState.dodgeIFrameEndTick) {
            return false;
        }

        UmbraPlayerState state = getStateSaveService().getOrCreatePlayerState(player.getUUID());
        boolean precision = tick - combatState.dodgeStartTick < PRECISION_DODGE_TICKS;
        if (precision) {
            state.setFatigue(Math.max(0, state.getFatigue() - DODGE_FATIGUE_COST));
            if (tick - combatState.lastPrecisionManaRewardTick >= PRECISION_MANA_COOLDOWN_TICKS) {
                state.setCurrentMana(Math.min(maximumMana(state), state.getCurrentMana() + calculatePrecisionManaRestore(maximumMana(state))));
                combatState.lastPrecisionManaRewardTick = tick;
            }
            net.minecraft.server.level.ServerLevel level = (net.minecraft.server.level.ServerLevel) player.level();
            level.sendParticles(ParticleTypes.PORTAL, player.getX(), player.getY() + 0.9, player.getZ(), 12, 0.25, 0.45, 0.25, 0.04);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 0.35F, 1.65F);
        }
        syncDodgeState(player, state, combatState, precision);
        return true;
    }

    private void syncCombatState(ServerPlayer player, PlayerCombatState state) {
        if (player.connection != null) {
            ServerPlayNetworking.send(player, new UmbraCombatStatePayload(state.inCombatStance, state.comboCount));
        }
    }

    private void syncDodgeState(ServerPlayer player, UmbraPlayerState state, PlayerCombatState combatState, boolean precisionDodge) {
        if (player.connection == null) {
            return;
        }
        long tick = ((net.minecraft.server.level.ServerLevel) player.level()).getServer().getTickCount();
        int ticksRemaining = (int) Math.max(0, combatState.dodgeActionEndTick - tick);
        ServerPlayNetworking.send(player, new UmbraDodgeStatePayload(
            (float) state.getCurrentMana(), (float) state.getCurrentFocus(), state.getFatigue(), ticksRemaining, precisionDodge
        ));
        combatState.lastResourceSyncTick = tick;
    }

    @Override
    public float calculateCustomDamage(ServerPlayer player, LivingEntity target, float originalDamage) {
        return calculateCustomDamageDetails(player, target, originalDamage).finalDmg();
    }

    @Override
    public CustomDamageDetails calculateCustomDamageDetails(ServerPlayer player, LivingEntity target, float originalDamage) {
        UUID uuid = player.getUUID();
        UmbraPlayerState playerState = getStateSaveService().getOrCreatePlayerState(uuid);
        long tick = ((net.minecraft.server.level.ServerLevel) player.level()).getServer() != null ? ((net.minecraft.server.level.ServerLevel) player.level()).getServer().getTickCount() : 0;
        PlayerCombatState combatState = getOrCreateCombatState(uuid, tick);

        // STR & Base damage
        int strength = playerState.getStrength();

        // Find WeaponBase from held main-hand item
        double weaponBase = 1.0;
        ItemStack stack = player.getMainHandItem();
        if (!stack.isEmpty()) {
            net.minecraft.world.item.Item item = stack.getItem();
            String path = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(item).getPath();

            if (path.contains("sword")) {
                if (path.contains("netherite")) weaponBase = 8.0;
                else if (path.contains("diamond")) weaponBase = 7.0;
                else if (path.contains("iron")) weaponBase = 6.0;
                else if (path.contains("stone")) weaponBase = 5.0;
                else weaponBase = 4.0;
            } else if (path.contains("axe")) {
                if (path.contains("netherite")) weaponBase = 10.0;
                else if (path.contains("diamond")) weaponBase = 9.0;
                else if (path.contains("iron")) weaponBase = 9.0;
                else if (path.contains("stone")) weaponBase = 9.0;
                else weaponBase = 7.0;
            } else if (path.contains("pickaxe")) {
                if (path.contains("netherite")) weaponBase = 6.0;
                else if (path.contains("diamond")) weaponBase = 5.0;
                else if (path.contains("iron")) weaponBase = 4.0;
                else weaponBase = 2.0;
            } else if (path.contains("shovel")) {
                if (path.contains("netherite")) weaponBase = 6.5;
                else if (path.contains("diamond")) weaponBase = 5.5;
                else if (path.contains("iron")) weaponBase = 4.5;
                else weaponBase = 2.5;
            } else {
                weaponBase = 1.0;
            }
        }

        // Perception & Critical check
        int perception = playerState.getPerception();
        int effPer = getEffectiveStat(perception);
        double critChance = 0.05 + effPer * 0.0025;
        if (critChance > 0.60) {
            critChance = 0.60;
        }

        boolean isCrit = random.nextDouble() < critChance;
        double cooldownScale = player.getAttackStrengthScale(0.5F);
        double randomVar = random.nextDouble();

        float finalDmg = calculateFormulaDamage(
            playerState.getLevel(),
            strength,
            perception,
            combatState.comboCount,
            (float) weaponBase,
            (float) cooldownScale,
            target.getArmorValue(),
            isCrit,
            target.invulnerableTime <= 10,
            randomVar
        );

        int effStr = getEffectiveStat(strength);
        double baseDmgVal = (weaponBase + effStr * 1.2) * cooldownScale;
        double comboMult = 1.0 + combatState.comboCount * 0.02;
        if (comboMult > 1.5) comboMult = 1.5;
        double critDamageMult = 1.50 + effPer * 0.005;
        if (critDamageMult > 2.50) critDamageMult = 2.50;
        double armorMitigation = target.getArmorValue() / (target.getArmorValue() + 50.0 + 10.0 * playerState.getLevel());
        if (armorMitigation > 0.75) armorMitigation = 0.75;

        return new CustomDamageDetails(
            (float) baseDmgVal,
            combatState.comboCount,
            (float) comboMult,
            isCrit,
            (float) (isCrit ? critDamageMult : 1.0),
            target.getArmorValue(),
            (float) armorMitigation,
            finalDmg
        );
    }

    public float calculateFormulaDamage(
        int level,
        int strength,
        int perception,
        int comboCount,
        float weaponBase,
        float cooldownScale,
        float targetArmor,
        boolean forceCrit,
        boolean applyVariance,
        double randomDoubleForVariance
    ) {
        int effStr = getEffectiveStat(strength);
        double baseDamage = (weaponBase + effStr * 1.2) * cooldownScale;

        double comboMult = 1.0 + comboCount * 0.02;
        if (comboMult > 1.5) {
            comboMult = 1.5;
        }

        double dmgGoc = baseDamage * comboMult;

        int effPer = getEffectiveStat(perception);
        double critDamageMult = 1.50 + effPer * 0.005;
        if (critDamageMult > 2.50) {
            critDamageMult = 2.50;
        }

        double critMult = forceCrit ? critDamageMult : 1.0;

        double armorMitigation = targetArmor / (targetArmor + 50.0 + 10.0 * level);
        if (armorMitigation > 0.75) {
            armorMitigation = 0.75;
        }

        double dmgFinal = dmgGoc * (1.0 - armorMitigation) * critMult;

        if (applyVariance) {
            double variance = 0.95 + randomDoubleForVariance * 0.10;
            dmgFinal = dmgFinal * variance;
        }

        return (float) dmgFinal;
    }

    private static int getEffectiveStat(int rawValue) {
        if (rawValue <= 100) {
            return rawValue;
        }
        return 100 + (int) Math.floor(Math.pow(rawValue - 100, 0.75));
    }

    @Override
    public void tick(MinecraftServer server) {
        long currentTick = server.getTickCount();
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            UUID uuid = player.getUUID();
            PlayerCombatState state = playerStates.get(uuid);
            if (state == null) continue;

            boolean changed = false;
            UmbraPlayerState resourceState = getStateSaveService().getOrCreatePlayerState(uuid);

            executeQueuedDodge(player, state, resourceState, currentTick);
            applyDodgeVelocity(player, state, currentTick);
            recoverCombatResources(resourceState, state, currentTick);

            // Decay combo if 3 seconds (60 ticks) of no attack activity
            if (state.comboCount > 0 && currentTick - state.lastAttackTick > COMBO_DECAY_TICKS) {
                state.comboCount = 0;
                changed = true;
            }

            // Stance timeout if 10 seconds (200 ticks) of no combat activity
            if (state.inCombatStance) {
                long ticksSinceAction = currentTick - state.lastCombatActionTick;
                long ticksSinceDamage = currentTick - state.lastDamageTick;
                if (ticksSinceAction > STANCE_DECAY_TICKS && ticksSinceDamage > STANCE_DECAY_TICKS) {
                    state.inCombatStance = false;
                    state.comboCount = 0;
                    changed = true;
                }
            }

            if (changed) {
                syncCombatState(player, state);
            }
            if (currentTick - state.lastResourceSyncTick >= RESOURCE_SYNC_INTERVAL_TICKS) {
                syncDodgeState(player, resourceState, state, false);
            }
        }
    }

    @Override
    public void clearPlayerState(UUID playerUuid) {
        playerStates.remove(playerUuid);
    }

    private void applyDodgeVelocity(ServerPlayer player, PlayerCombatState state, long currentTick) {
        long step = currentTick - state.dodgeStartTick;
        if (state.dodgeDirection == null || step < 0 || step >= DODGE_VELOCITY_CURVE.length) {
            return;
        }
        double yaw = Math.toRadians(player.getYRot());
        double x = state.dodgeDirection.worldXForYawRadians(yaw);
        double z = state.dodgeDirection.worldZForYawRadians(yaw);
        double length = Math.sqrt(x * x + z * z);
        if (length <= 0.0) {
            return;
        }
        double speed = DODGE_VELOCITY_CURVE[(int) step];
        player.setDeltaMovement(new Vec3(x / length * speed, player.getDeltaMovement().y, z / length * speed));
        player.hurtMarked = true;
    }

    private void executeQueuedDodge(ServerPlayer player, PlayerCombatState combatState, UmbraPlayerState resourceState, long currentTick) {
        if (combatState.pendingDodgeDirection == null || currentTick < combatState.dodgeActionEndTick) {
            return;
        }
        DodgeDirection direction = combatState.pendingDodgeDirection;
        combatState.pendingDodgeDirection = null;
        if (currentTick <= combatState.pendingDodgeExpireTick) {
            startDodge(player, direction, currentTick, combatState, resourceState);
        }
    }

    private void recoverCombatResources(UmbraPlayerState state, PlayerCombatState combatState, long currentTick) {
        double maximumMana = maximumMana(state);
        if (state.getCurrentMana() > maximumMana) {
            state.setCurrentMana(maximumMana);
        }
        if (currentTick >= combatState.focusRecoveryLockedUntilTick) {
            int effectiveAgility = getEffectiveStat(state.getAgility());
            double focusPerSecond = 18.0 + Math.min(12.0, 0.12 * effectiveAgility);
            if (currentTick - combatState.lastDamageTick <= 20) {
                focusPerSecond *= 0.65;
            }
            state.setCurrentFocus(Math.min(100.0, state.getCurrentFocus() + focusPerSecond / 20.0));
        }
        double manaPerSecond = 2.0 + state.getIntelligence() * 0.15;
        if (currentTick - combatState.lastCombatActionTick > 100 && currentTick - combatState.lastDamageTick > 100) {
            manaPerSecond *= 1.5;
        }
        state.setCurrentMana(Math.min(maximumMana, state.getCurrentMana() + manaPerSecond / 20.0));
    }

    public static int calculateDodgeIFrameTicksForAgility(int agility) {
        double seconds = Math.min(0.4, 0.25 + getEffectiveStat(agility) * 0.001);
        return (int) Math.ceil(seconds * 20.0);
    }

    public static int calculateDodgeActionTicks() {
        return DODGE_VELOCITY_CURVE.length;
    }

    public static double calculateDodgeUnobstructedDistance() {
        double distance = 0.0;
        for (double velocity : DODGE_VELOCITY_CURVE) {
            distance += velocity;
        }
        return distance;
    }

    public static double calculateDodgeFocusCost(int fatigue) {
        return fatigue >= 85 ? DODGE_FOCUS_COST * 1.5 : DODGE_FOCUS_COST;
    }

    public static double calculatePrecisionManaRestore(double maximumMana) {
        return Math.min(maximumMana * 0.02, 6.0);
    }

    private double maximumMana(UmbraPlayerState state) {
        return 20.0 + getEffectiveStat(state.getIntelligence()) * 8.0 + state.getLevel();
    }
}

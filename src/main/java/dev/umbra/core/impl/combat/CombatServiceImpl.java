package dev.umbra.core.impl.combat;

import dev.umbra.UmbraMod;
import dev.umbra.core.contract.combat.CombatService;
import dev.umbra.core.contract.combat.UmbraCombatStatePayload;
import dev.umbra.core.contract.state.StateSaveService;
import dev.umbra.core.contract.state.UmbraPlayerState;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Random;

public final class CombatServiceImpl implements CombatService {
    private static final int COMBO_DECAY_TICKS = 60; // 3 seconds
    private static final int STANCE_DECAY_TICKS = 200; // 10 seconds

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
    }

    private PlayerCombatState getOrCreateCombatState(UUID uuid, long currentTick) {
        return playerStates.computeIfAbsent(uuid, k -> {
            PlayerCombatState state = new PlayerCombatState();
            state.lastAttackTick = currentTick;
            state.lastDamageTick = currentTick;
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
        if (!state.inCombatStance) {
            state.inCombatStance = true;
            syncCombatState(player, state);
        }
    }

    private void syncCombatState(ServerPlayer player, PlayerCombatState state) {
        if (player.connection != null) {
            ServerPlayNetworking.send(player, new UmbraCombatStatePayload(state.inCombatStance, state.comboCount));
        }
    }

    @Override
    public float calculateCustomDamage(ServerPlayer player, LivingEntity target, float originalDamage) {
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
        double randomVar = random.nextDouble();

        float finalDmg = calculateFormulaDamage(
            playerState.getLevel(),
            strength,
            perception,
            combatState.comboCount,
            (float) weaponBase,
            target.getArmorValue(),
            isCrit,
            true,
            randomVar
        );

        // Logging detailed combat stats to Dummy Entity if target is Combat Dummy
        if (target instanceof CombatDummyEntity dummy) {
            int effStr = getEffectiveStat(strength);
            double baseDmgVal = weaponBase + effStr * 1.2;
            double comboMult = 1.0 + combatState.comboCount * 0.02;
            if (comboMult > 1.5) comboMult = 1.5;
            double critDamageMult = 1.50 + effPer * 0.005;
            if (critDamageMult > 2.50) critDamageMult = 2.50;
            double armorMitigation = target.getArmorValue() / (target.getArmorValue() + 50.0 + 10.0 * playerState.getLevel());
            if (armorMitigation > 0.75) armorMitigation = 0.75;

            dummy.logCombatHit(
                player,
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

        return finalDmg;
    }

    public float calculateFormulaDamage(
        int level,
        int strength,
        int perception,
        int comboCount,
        float weaponBase,
        float targetArmor,
        boolean forceCrit,
        boolean applyVariance,
        double randomDoubleForVariance
    ) {
        int effStr = getEffectiveStat(strength);
        double baseDamage = weaponBase + effStr * 1.2;

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

    private int getEffectiveStat(int rawValue) {
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

            // Decay combo if 3 seconds (60 ticks) of no attack activity
            if (state.comboCount > 0 && currentTick - state.lastAttackTick > COMBO_DECAY_TICKS) {
                state.comboCount = 0;
                changed = true;
            }

            // Stance timeout if 10 seconds (200 ticks) of no combat activity
            if (state.inCombatStance) {
                long ticksSinceAttack = currentTick - state.lastAttackTick;
                long ticksSinceDamage = currentTick - state.lastDamageTick;
                if (ticksSinceAttack > STANCE_DECAY_TICKS && ticksSinceDamage > STANCE_DECAY_TICKS) {
                    state.inCombatStance = false;
                    state.comboCount = 0;
                    changed = true;
                }
            }

            if (changed) {
                syncCombatState(player, state);
            }
        }
    }

    @Override
    public void clearPlayerState(UUID playerUuid) {
        playerStates.remove(playerUuid);
    }
}

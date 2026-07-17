package dev.umbra.core.impl.progression;

import dev.umbra.UmbraMod;
import dev.umbra.core.contract.context.BoundedContext;
import dev.umbra.core.contract.event.EventEnvelope;
import dev.umbra.core.contract.event.UmbraEventBus;
import dev.umbra.core.contract.progression.ProgressionService;
import dev.umbra.core.contract.progression.UmbraPlayerLevelUpEvent;
import dev.umbra.core.contract.progression.UmbraPlayerXpChangedEvent;
import dev.umbra.core.contract.state.StateSaveService;
import dev.umbra.core.contract.state.UmbraPlayerState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import java.util.UUID;

/**
 * Server-authoritative implementation of ProgressionService.
 */
public final class ProgressionServiceImpl implements ProgressionService {

    private StateSaveService getStateSaveService() {
        return UmbraMod.getServiceRegistry()
            .locate(StateSaveService.class)
            .orElseThrow(() -> new IllegalStateException("StateSaveService not registered"));
    }

    private UmbraEventBus getEventBus() {
        return UmbraMod.getServiceRegistry()
            .locate(UmbraEventBus.class)
            .orElseThrow(() -> new IllegalStateException("UmbraEventBus not registered"));
    }

    @Override
    public int getRequiredXpForLevel(int level) {
        if (level < 1) {
            return 0;
        }
        if (level < 100) {
            return (int) Math.floor(60.0 * Math.pow(level, 1.85) + 25.0 * level);
        } else {
            int p = level - 99; // Prestige level (P = level - 99 >= 1)
            return 50000 + p * 17500;
        }
    }

    @Override
    public void addXp(UUID playerUuid, int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("XP amount cannot be negative");
        }
        if (amount == 0) {
            return;
        }

        UmbraPlayerState state = getStateSaveService().getOrCreatePlayerState(playerUuid);
        int oldLevel = state.getLevel();
        int oldXp = state.getShadowXp();

        int currentLevel = oldLevel;
        int currentXp = oldXp + amount;
        int requiredXp = getRequiredXpForLevel(currentLevel);

        boolean leveledUp = false;
        while (currentXp >= requiredXp) {
            currentXp -= requiredXp;
            currentLevel++;
            leveledUp = true;
            requiredXp = getRequiredXpForLevel(currentLevel);
        }

        state.setShadowXp(currentXp);
        if (leveledUp) {
            int levelDiff = currentLevel - oldLevel;
            if (levelDiff > 0) {
                state.setStatPoints(state.getStatPoints() + levelDiff * 5);
            }
            state.setLevel(currentLevel);
        }

        // Publish events
        UmbraEventBus eventBus = getEventBus();
        eventBus.publish(new EventEnvelope<>(
            BoundedContext.PROGRESSION,
            new UmbraPlayerXpChangedEvent(playerUuid, oldXp, currentXp, currentLevel)
        ));

        if (leveledUp) {
            eventBus.publish(new EventEnvelope<>(
                BoundedContext.PROGRESSION,
                new UmbraPlayerLevelUpEvent(playerUuid, oldLevel, currentLevel)
            ));
        }
    }

    @Override
    public void addXp(ServerPlayer player, int amount) {
        if (player == null) return;
        UUID uuid = player.getUUID();
        UmbraPlayerState state = getStateSaveService().getOrCreatePlayerState(uuid);
        int oldLevel = state.getLevel();

        addXp(uuid, amount);

        int newLevel = state.getLevel();
        updateDerivedAttributes(player);
        if (newLevel > oldLevel) {
            // Apply level-up effects for online player
            player.setHealth(player.getMaxHealth());
            player.level().playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.PLAYER_LEVELUP,
                SoundSource.PLAYERS,
                1.0F,
                1.0F
            );
        }

        // Sync client
        getStateSaveService().syncPlayerState(player);
    }

    @Override
    public void setXp(UUID playerUuid, int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("XP amount cannot be negative");
        }

        UmbraPlayerState state = getStateSaveService().getOrCreatePlayerState(playerUuid);
        int oldXp = state.getShadowXp();
        int currentLevel = state.getLevel();

        // Clamp to maximum required for level (if it exceeds, it should level up, but setXp sets the XP directly)
        int requiredXp = getRequiredXpForLevel(currentLevel);
        int targetXp = amount;

        boolean leveledUp = false;
        int oldLevel = currentLevel;
        while (targetXp >= requiredXp) {
            targetXp -= requiredXp;
            currentLevel++;
            leveledUp = true;
            requiredXp = getRequiredXpForLevel(currentLevel);
        }

        state.setShadowXp(targetXp);
        if (leveledUp) {
            int levelDiff = currentLevel - oldLevel;
            if (levelDiff > 0) {
                state.setStatPoints(state.getStatPoints() + levelDiff * 5);
            }
            state.setLevel(currentLevel);
        }

        UmbraEventBus eventBus = getEventBus();
        eventBus.publish(new EventEnvelope<>(
            BoundedContext.PROGRESSION,
            new UmbraPlayerXpChangedEvent(playerUuid, oldXp, targetXp, currentLevel)
        ));

        if (leveledUp) {
            eventBus.publish(new EventEnvelope<>(
                BoundedContext.PROGRESSION,
                new UmbraPlayerLevelUpEvent(playerUuid, oldLevel, currentLevel)
            ));
        }
    }

    @Override
    public void setXp(ServerPlayer player, int amount) {
        if (player == null) return;
        UUID uuid = player.getUUID();
        UmbraPlayerState state = getStateSaveService().getOrCreatePlayerState(uuid);
        int oldLevel = state.getLevel();

        setXp(uuid, amount);

        int newLevel = state.getLevel();
        updateDerivedAttributes(player);
        if (newLevel > oldLevel) {
            player.setHealth(player.getMaxHealth());
            player.level().playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.PLAYER_LEVELUP,
                SoundSource.PLAYERS,
                1.0F,
                1.0F
            );
        }

        getStateSaveService().syncPlayerState(player);
    }

    @Override
    public void setLevel(UUID playerUuid, int level) {
        if (level < 1) {
            throw new IllegalArgumentException("Level must be at least 1");
        }

        UmbraPlayerState state = getStateSaveService().getOrCreatePlayerState(playerUuid);
        int oldLevel = state.getLevel();
        if (oldLevel == level) {
            return;
        }

        int levelDiff = level - oldLevel;
        state.setStatPoints(Math.max(0, state.getStatPoints() + levelDiff * 5));
        state.setLevel(level);
        // Reset XP on direct level changes to prevent out of bounds
        int oldXp = state.getShadowXp();
        state.setShadowXp(0);

        UmbraEventBus eventBus = getEventBus();
        eventBus.publish(new EventEnvelope<>(
            BoundedContext.PROGRESSION,
            new UmbraPlayerXpChangedEvent(playerUuid, oldXp, 0, level)
        ));

        eventBus.publish(new EventEnvelope<>(
            BoundedContext.PROGRESSION,
            new UmbraPlayerLevelUpEvent(playerUuid, oldLevel, level)
        ));
    }

    @Override
    public void setLevel(ServerPlayer player, int level) {
        if (player == null) return;
        UUID uuid = player.getUUID();
        int oldLevel = playerLevel(uuid);

        setLevel(uuid, level);

        updateDerivedAttributes(player);
        if (level > oldLevel) {
            player.setHealth(player.getMaxHealth());
            player.level().playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.PLAYER_LEVELUP,
                SoundSource.PLAYERS,
                1.0F,
                1.0F
            );
        }

        getStateSaveService().syncPlayerState(player);
    }

    @Override
    public void updateDerivedAttributes(ServerPlayer player) {
        if (player == null) return;
        UmbraPlayerState state = getStateSaveService().getOrCreatePlayerState(player.getUUID());
        int level = state.getLevel();
        int vitality = state.getVitality();
        float maxHp = 20.0f + getEffectiveStat(vitality) * 6.0f + level * 2.0f;

        var attributeInstance = player.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH);
        if (attributeInstance != null) {
            attributeInstance.setBaseValue(maxHp);
            if (player.getHealth() > maxHp) {
                player.setHealth(maxHp);
            }
        }
    }

    private int getEffectiveStat(int rawValue) {
        if (rawValue <= 100) {
            return rawValue;
        }
        return 100 + (int) Math.floor(Math.pow(rawValue - 100, 0.75));
    }

    private int playerLevel(UUID uuid) {
        return getStateSaveService().getOrCreatePlayerState(uuid).getLevel();
    }
}

package dev.umbra.core.contract.combat;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import java.util.UUID;

/**
 * Service responsible for server-authoritative player combat state management
 * (stance, combos) and custom damage pipeline calculations.
 */
public interface CombatService {
    /**
     * Checks if a player is currently in combat stance.
     */
    boolean isInCombatStance(UUID playerUuid);

    /**
     * Returns the current combo count for a player.
     */
    int getComboCount(UUID playerUuid);

    /**
     * Forces a player into combat stance.
     */
    void enterCombatStance(ServerPlayer player);

    /**
     * Forces a player out of combat stance.
     */
    void exitCombatStance(ServerPlayer player);

    /**
     * Triggers combo increment and stance activation when a player attacks an entity.
     */
    void registerAttack(ServerPlayer player, LivingEntity target);

    /**
     * Triggers stance activation when a player is damaged.
     */
    void registerDamage(ServerPlayer player);

    /**
     * Computes final damage according to the UMBRA damage pipeline.
     */
    float calculateCustomDamage(ServerPlayer player, LivingEntity target, float originalDamage);

    /**
     * Ticks the combat service to handle stance timeouts and combo decay.
     */
    void tick(net.minecraft.server.MinecraftServer server);

    /**
     * Clears all combat tracking state for a player (e.g. on log out).
     */
    void clearPlayerState(UUID playerUuid);
}

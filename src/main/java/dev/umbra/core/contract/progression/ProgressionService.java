package dev.umbra.core.contract.progression;

import net.minecraft.server.level.ServerPlayer;
import java.util.UUID;

/**
 * Service responsible for managing server-authoritative player shadow XP and level.
 */
public interface ProgressionService {
    /**
     * Awards shadow XP to a player and triggers level up if the threshold is met.
     * Only callable on the server side.
     */
    void addXp(UUID playerUuid, int amount);

    /**
     * Awards shadow XP to an online player, triggers level up, heals the player,
     * plays a sound, and syncs status to the client.
     */
    void addXp(ServerPlayer player, int amount);

    /**
     * Sets the shadow XP of a player.
     * Only callable on the server side.
     */
    void setXp(UUID playerUuid, int amount);

    /**
     * Sets the shadow XP of an online player and syncs status to the client.
     */
    void setXp(ServerPlayer player, int amount);

    /**
     * Sets the level of a player.
     * Only callable on the server side.
     */
    void setLevel(UUID playerUuid, int level);

    /**
     * Sets the level of an online player, handles health restoration, and syncs status to the client.
     */
    void setLevel(ServerPlayer player, int level);

    /**
     * Returns the required XP to level up from the given level.
     */
    int getRequiredXpForLevel(int level);
}

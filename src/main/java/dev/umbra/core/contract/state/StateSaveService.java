package dev.umbra.core.contract.state;

import java.nio.file.Path;
import java.util.UUID;

/**
 * Service responsible for managing, migrating, and persisting Player and World states.
 */
public interface StateSaveService {
    /**
     * Gets the current cached state of a player, or creates a default one if not present.
     */
    UmbraPlayerState getOrCreatePlayerState(UUID playerUuid);

    /**
     * Gets the global world state.
     */
    UmbraWorldState getWorldState();

    /**
     * Deserializes and migrates a JSON string into a player state, caching it.
     */
    void loadPlayerState(UUID playerUuid, String json) throws Exception;

    /**
     * Deserializes and migrates a JSON string into the world state.
     */
    void loadWorldState(String json) throws Exception;

    /**
     * Serializes a player state to a JSON string.
     */
    String savePlayerState(UUID playerUuid) throws Exception;

    /**
     * Serializes the world state to a JSON string.
     */
    String saveWorldState() throws Exception;

    /**
     * Event triggered when a player joins the server. Loads state from disk.
     */
    void onPlayerJoin(UUID playerUuid, Path worldDir);

    /**
     * Event triggered when a player disconnects from the server. Saves state to disk and clears cache.
     */
    void onPlayerLeave(UUID playerUuid, Path worldDir);

    /**
     * Event triggered during periodic autosaves or manual world saves. Saves all states.
     */
    void onWorldSave(Path worldDir);

    /**
     * Event triggered when the server starts. Loads global world state from disk.
     */
    void onServerStart(Path worldDir);

    /**
     * Event triggered when the server stops. Saves all states and cleans up cache.
     */
    void onServerStop(Path worldDir);
}

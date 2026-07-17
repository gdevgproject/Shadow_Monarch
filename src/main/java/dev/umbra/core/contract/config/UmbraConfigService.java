package dev.umbra.core.contract.config;

/**
 * Service managing the configuration lifecycle and access to the 3-layer settings.
 */
public interface UmbraConfigService {
    /**
     * Gets the active Player configuration.
     */
    PlayerConfig getPlayerConfig();

    /**
     * Gets the active Server configuration.
     */
    ServerConfig getServerConfig();

    /**
     * Gets the active Dev configuration.
     */
    DevConfig getDevConfig();

    /**
     * Loads the configuration file from disk.
     */
    void load();

    /**
     * Saves the current configuration values to disk.
     */
    void save();

    /**
     * Reloads the configuration file from disk dynamically, preserving other runtime state.
     */
    void reload();
}

package dev.umbra.core.impl.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.umbra.UmbraMod;
import dev.umbra.core.contract.config.DevConfig;
import dev.umbra.core.contract.config.PlayerConfig;
import dev.umbra.core.contract.config.ServerConfig;
import dev.umbra.core.contract.config.UmbraConfigService;
import net.fabricmc.loader.api.FabricLoader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * GSON-based implementation of the 3-layer configuration service.
 */
public final class UmbraConfigServiceImpl implements UmbraConfigService {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Path configPath;

    private PlayerConfig playerConfig = new PlayerConfig();
    private ServerConfig serverConfig = new ServerConfig();
    private DevConfig devConfig = new DevConfig();

    public UmbraConfigServiceImpl() {
        Path configDir;
        try {
            configDir = FabricLoader.getInstance().getConfigDir();
        } catch (Throwable t) {
            // Fallback for environment running outside a Fabric container (like test suites)
            configDir = Path.of("config");
        }
        this.configPath = configDir.resolve("umbra.json");
    }

    public UmbraConfigServiceImpl(Path configPath) {
        this.configPath = configPath;
    }

    @Override
    public PlayerConfig getPlayerConfig() {
        return playerConfig;
    }

    @Override
    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    @Override
    public DevConfig getDevConfig() {
        return devConfig;
    }

    @Override
    public void load() {
        if (!Files.exists(configPath)) {
            UmbraMod.LOGGER.info("Config file does not exist, generating default config.");
            save();
            return;
        }

        try {
            String jsonContent = Files.readString(configPath, StandardCharsets.UTF_8);
            parseJson(jsonContent);
        } catch (Exception e) {
            UmbraMod.LOGGER.error("Failed to load config: {}. Falling back to default settings.", e.getMessage(), e);
            resetToDefaults();
        }
    }

    @Override
    public void save() {
        try {
            JsonObject root = new JsonObject();
            root.add("player", GSON.toJsonTree(playerConfig));
            root.add("server", GSON.toJsonTree(serverConfig));
            root.add("dev", GSON.toJsonTree(devConfig));

            if (configPath.getParent() != null) {
                Files.createDirectories(configPath.getParent());
            }
            Files.writeString(configPath, GSON.toJson(root), StandardCharsets.UTF_8);
            UmbraMod.LOGGER.info("Config successfully saved to {}", configPath);
        } catch (IOException e) {
            UmbraMod.LOGGER.error("Failed to save config: {}", e.getMessage(), e);
        }
    }

    @Override
    public void reload() {
        UmbraMod.LOGGER.info("Reloading config dynamically from {}", configPath);
        load();
    }

    private void parseJson(String jsonContent) {
        JsonObject root = JsonParser.parseString(jsonContent).getAsJsonObject();

        if (root.has("player")) {
            try {
                PlayerConfig newPlayer = GSON.fromJson(root.get("player"), PlayerConfig.class);
                if (newPlayer != null) {
                    this.playerConfig = newPlayer;
                }
            } catch (Exception e) {
                UmbraMod.LOGGER.error("Failed to parse player config layer, using default: {}", e.getMessage());
            }
        }

        if (root.has("server")) {
            try {
                ServerConfig newServer = GSON.fromJson(root.get("server"), ServerConfig.class);
                if (newServer != null) {
                    this.serverConfig = newServer;
                }
            } catch (Exception e) {
                UmbraMod.LOGGER.error("Failed to parse server config layer, using default: {}", e.getMessage());
            }
        }

        if (root.has("dev")) {
            try {
                DevConfig newDev = GSON.fromJson(root.get("dev"), DevConfig.class);
                if (newDev != null) {
                    this.devConfig = newDev;
                }
            } catch (Exception e) {
                UmbraMod.LOGGER.error("Failed to parse dev config layer, using default: {}", e.getMessage());
            }
        }
    }

    private void resetToDefaults() {
        this.playerConfig = new PlayerConfig();
        this.serverConfig = new ServerConfig();
        this.devConfig = new DevConfig();
    }
}

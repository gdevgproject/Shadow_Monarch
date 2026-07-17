package dev.umbra.core.impl.state;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.umbra.UmbraMod;
import dev.umbra.core.contract.state.StateSaveService;
import dev.umbra.core.contract.state.UmbraPlayerState;
import dev.umbra.core.contract.state.UmbraPlayerStatePayload;
import dev.umbra.core.contract.state.UmbraWorldState;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of StateSaveService handling saving, loading, and migration of states.
 */
public final class StateSaveServiceImpl implements StateSaveService {
    public static final int TARGET_PLAYER_VERSION = 4;
    public static final int TARGET_WORLD_VERSION = 2;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Map<UUID, UmbraPlayerState> activePlayerStates = new ConcurrentHashMap<>();
    private final UmbraWorldState worldState = new UmbraWorldState();

    private final StateMigrationChain playerMigrationChain = new StateMigrationChain();
    private final StateMigrationChain worldMigrationChain = new StateMigrationChain();

    public StateSaveServiceImpl() {
        // Register migrations
        playerMigrationChain.registerMigration(new PlayerMigrationV1ToV2());
        playerMigrationChain.registerMigration(new PlayerMigrationV2ToV3());
        playerMigrationChain.registerMigration(new PlayerMigrationV3ToV4());
        worldMigrationChain.registerMigration(new WorldMigrationV1ToV2());
    }

    @Override
    public UmbraPlayerState getOrCreatePlayerState(UUID playerUuid) {
        return activePlayerStates.computeIfAbsent(playerUuid, uuid -> new UmbraPlayerState());
    }

    @Override
    public UmbraWorldState getWorldState() {
        return worldState;
    }

    @Override
    public void loadPlayerState(UUID playerUuid, String json) throws Exception {
        JsonObject parsed = JsonParser.parseString(json).getAsJsonObject();
        JsonObject migrated = playerMigrationChain.migrate(parsed, TARGET_PLAYER_VERSION);

        UmbraPlayerState state = new UmbraPlayerState();
        state.setSchemaVersion(migrated.get("schema_version").getAsInt());
        state.setLevel(migrated.get("level").getAsInt());
        state.setShadowXp(migrated.has("shadow_xp") ? migrated.get("shadow_xp").getAsInt() : 0);
        state.setRank(migrated.has("rank") ? migrated.get("rank").getAsString() : "E");
        state.setStrength(migrated.has("strength") ? migrated.get("strength").getAsInt() : 10);
        state.setAgility(migrated.has("agility") ? migrated.get("agility").getAsInt() : 10);
        state.setVitality(migrated.has("vitality") ? migrated.get("vitality").getAsInt() : 10);
        state.setIntelligence(migrated.has("intelligence") ? migrated.get("intelligence").getAsInt() : 10);
        state.setPerception(migrated.has("perception") ? migrated.get("perception").getAsInt() : 10);
        state.setStatPoints(migrated.has("stat_points") ? migrated.get("stat_points").getAsInt() : 0);
        state.setEssence(migrated.has("essence") ? migrated.get("essence").getAsInt() : 0);
        state.setJobChanged(migrated.has("job_changed") && migrated.get("job_changed").getAsBoolean());
        state.setLastRespecTime(migrated.has("last_respec_time") ? migrated.get("last_respec_time").getAsLong() : 0L);
        state.setCurrentMana(migrated.has("current_mana") ? migrated.get("current_mana").getAsDouble() : 20.0 + state.getIntelligence() * 8.0 + state.getLevel());
        state.setCurrentFocus(migrated.has("current_focus") ? migrated.get("current_focus").getAsDouble() : 100.0);
        state.setFatigue(migrated.has("fatigue") ? migrated.get("fatigue").getAsInt() : 0);

        // Parse legacy/unrecognized fields
        state.getLegacyFields().clear();
        for (Map.Entry<String, JsonElement> entry : migrated.entrySet()) {
            String key = entry.getKey();
            if (!key.equals("schema_version") && !key.equals("level") &&
                !key.equals("shadow_xp") && !key.equals("rank") &&
                !key.equals("strength") && !key.equals("agility") &&
                !key.equals("vitality") && !key.equals("intelligence") &&
                !key.equals("perception") && !key.equals("stat_points") &&
                !key.equals("essence") && !key.equals("job_changed") &&
                !key.equals("last_respec_time") && !key.equals("current_mana") &&
                !key.equals("current_focus") && !key.equals("fatigue")) {
                state.getLegacyFields().put(key, entry.getValue());
            }
        }

        activePlayerStates.put(playerUuid, state);
    }

    @Override
    public void loadWorldState(String json) throws Exception {
        JsonObject parsed = JsonParser.parseString(json).getAsJsonObject();
        JsonObject migrated = worldMigrationChain.migrate(parsed, TARGET_WORLD_VERSION);

        worldState.setSchemaVersion(migrated.get("schema_version").getAsInt());
        worldState.setActiveStratum(migrated.has("active_stratum") ? migrated.get("active_stratum").getAsInt() : 0);

        worldState.getClearedGates().clear();
        if (migrated.has("cleared_gates")) {
            for (JsonElement el : migrated.getAsJsonArray("cleared_gates")) {
                worldState.getClearedGates().add(el.getAsString());
            }
        }

        // Parse legacy/unrecognized fields
        worldState.getLegacyFields().clear();
        for (Map.Entry<String, JsonElement> entry : migrated.entrySet()) {
            String key = entry.getKey();
            if (!key.equals("schema_version") && !key.equals("active_stratum") && !key.equals("cleared_gates")) {
                worldState.getLegacyFields().put(key, entry.getValue());
            }
        }
    }

    @Override
    public String savePlayerState(UUID playerUuid) throws Exception {
        UmbraPlayerState state = getOrCreatePlayerState(playerUuid);
        JsonObject json = new JsonObject();
        json.addProperty("schema_version", TARGET_PLAYER_VERSION);
        json.addProperty("level", state.getLevel());
        json.addProperty("shadow_xp", state.getShadowXp());
        json.addProperty("rank", state.getRank());
        json.addProperty("strength", state.getStrength());
        json.addProperty("agility", state.getAgility());
        json.addProperty("vitality", state.getVitality());
        json.addProperty("intelligence", state.getIntelligence());
        json.addProperty("perception", state.getPerception());
        json.addProperty("stat_points", state.getStatPoints());
        json.addProperty("essence", state.getEssence());
        json.addProperty("job_changed", state.isJobChanged());
        json.addProperty("last_respec_time", state.getLastRespecTime());
        json.addProperty("current_mana", state.getCurrentMana());
        json.addProperty("current_focus", state.getCurrentFocus());
        json.addProperty("fatigue", state.getFatigue());

        // Merge legacy fields
        for (Map.Entry<String, JsonElement> entry : state.getLegacyFields().entrySet()) {
            json.add(entry.getKey(), entry.getValue());
        }

        return GSON.toJson(json);
    }

    @Override
    public String saveWorldState() throws Exception {
        JsonObject json = new JsonObject();
        json.addProperty("schema_version", TARGET_WORLD_VERSION);
        json.addProperty("active_stratum", worldState.getActiveStratum());

        Gson basicGson = new Gson();
        json.add("cleared_gates", basicGson.toJsonTree(worldState.getClearedGates()));

        // Merge legacy fields
        for (Map.Entry<String, JsonElement> entry : worldState.getLegacyFields().entrySet()) {
            json.add(entry.getKey(), entry.getValue());
        }

        return GSON.toJson(json);
    }

    @Override
    public void onPlayerJoin(UUID playerUuid, Path worldDir) {
        Path playerFile = getPlayerFilePath(worldDir, playerUuid);
        if (Files.exists(playerFile)) {
            try {
                String json = Files.readString(playerFile, StandardCharsets.UTF_8);
                loadPlayerState(playerUuid, json);
                UmbraMod.LOGGER.info("Successfully loaded state for player {}", playerUuid);
            } catch (Exception e) {
                UmbraMod.LOGGER.error("Failed to load player state for {}: {}", playerUuid, e.getMessage(), e);
                // Fallback to default
                activePlayerStates.put(playerUuid, new UmbraPlayerState());
            }
        } else {
            // New player
            activePlayerStates.put(playerUuid, new UmbraPlayerState());
        }
    }

    @Override
    public void onPlayerLeave(UUID playerUuid, Path worldDir) {
        if (activePlayerStates.containsKey(playerUuid)) {
            try {
                String json = savePlayerState(playerUuid);
                Path playerFile = getPlayerFilePath(worldDir, playerUuid);
                Files.createDirectories(playerFile.getParent());
                Files.writeString(playerFile, json, StandardCharsets.UTF_8);
                UmbraMod.LOGGER.info("Successfully saved state for player {}", playerUuid);
            } catch (Exception e) {
                UmbraMod.LOGGER.error("Failed to save player state for {}: {}", playerUuid, e.getMessage(), e);
            } finally {
                activePlayerStates.remove(playerUuid);
            }
        }
    }

    @Override
    public void onWorldSave(Path worldDir) {
        // Save world state
        try {
            String json = saveWorldState();
            Path worldFile = getWorldFilePath(worldDir);
            Files.createDirectories(worldFile.getParent());
            Files.writeString(worldFile, json, StandardCharsets.UTF_8);
            UmbraMod.LOGGER.info("Successfully saved world state");
        } catch (Exception e) {
            UmbraMod.LOGGER.error("Failed to save world state: {}", e.getMessage(), e);
        }

        // Save all cached active players
        for (UUID uuid : activePlayerStates.keySet()) {
            try {
                String json = savePlayerState(uuid);
                Path playerFile = getPlayerFilePath(worldDir, uuid);
                Files.createDirectories(playerFile.getParent());
                Files.writeString(playerFile, json, StandardCharsets.UTF_8);
            } catch (Exception e) {
                UmbraMod.LOGGER.error("Failed to autosave player state for {}: {}", uuid, e.getMessage(), e);
            }
        }
    }

    @Override
    public void onServerStart(Path worldDir) {
        Path worldFile = getWorldFilePath(worldDir);
        if (Files.exists(worldFile)) {
            try {
                String json = Files.readString(worldFile, StandardCharsets.UTF_8);
                loadWorldState(json);
                UmbraMod.LOGGER.info("Successfully loaded world state");
            } catch (Exception e) {
                UmbraMod.LOGGER.error("Failed to load world state: {}", e.getMessage(), e);
            }
        }
    }

    @Override
    public void onServerStop(Path worldDir) {
        // Force save everything on stop
        onWorldSave(worldDir);
        activePlayerStates.clear();
    }

    @Override
    public void syncPlayerState(net.minecraft.server.level.ServerPlayer player) {
        if (player == null) return;
        java.util.UUID playerUuid = player.getUUID();
        UmbraPlayerState state = getOrCreatePlayerState(playerUuid);
        net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(
            player,
            new UmbraPlayerStatePayload(
                state.getLevel(),
                state.getShadowXp(),
                state.getRank(),
                state.getStrength(),
                state.getAgility(),
                state.getVitality(),
                state.getIntelligence(),
                state.getPerception(),
                state.getStatPoints(),
                state.getEssence(),
                state.isJobChanged(),
                state.getLastRespecTime(),
                (float) state.getCurrentMana(),
                (float) state.getCurrentFocus(),
                state.getFatigue()
            )
        );
    }

    private Path getPlayerFilePath(Path worldDir, java.util.UUID playerUuid) {
        return worldDir.resolve("umbra/players/" + playerUuid.toString() + ".json");
    }

    private Path getWorldFilePath(Path worldDir) {
        return worldDir.resolve("umbra/world_state.json");
    }
}

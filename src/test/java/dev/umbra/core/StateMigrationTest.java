package dev.umbra.core;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.umbra.core.contract.state.UmbraPlayerState;
import dev.umbra.core.contract.state.UmbraWorldState;
import dev.umbra.core.impl.state.StateSaveServiceImpl;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public final class StateMigrationTest {

    private StateSaveServiceImpl saveService;
    private UUID testPlayerUuid;

    @BeforeEach
    public void setUp() {
        saveService = new StateSaveServiceImpl();
        testPlayerUuid = UUID.randomUUID();
    }

    private String readFixture(String filename) throws IOException {
        try (var stream = StateMigrationTest.class.getResourceAsStream("/fixtures/" + filename)) {
            if (stream == null) {
                throw new FileNotFoundException("Fixture not found: " + filename);
            }
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    @Test
    public void testPlayerMigrationAndLegacyPreservation() throws Exception {
        String v1Json = readFixture("player_v1.json");
        saveService.loadPlayerState(testPlayerUuid, v1Json);

        UmbraPlayerState state = saveService.getOrCreatePlayerState(testPlayerUuid);
        assertEquals(3, state.getSchemaVersion());
        assertEquals(12, state.getLevel());
        assertEquals(3400, state.getShadowXp());
        assertEquals("E", state.getRank());
        assertEquals(10, state.getStrength());
        assertEquals(10, state.getAgility());
        assertEquals(10, state.getVitality());
        assertEquals(10, state.getIntelligence());
        assertEquals(10, state.getPerception());
        assertEquals(55, state.getStatPoints()); // (12 - 1) * 5
        assertEquals(0, state.getEssence());
        assertFalse(state.isJobChanged());
        assertEquals(0L, state.getLastRespecTime());
        assertTrue(state.getLegacyFields().containsKey("custom_legacy_field"));
        assertEquals("some_value", state.getLegacyFields().get("custom_legacy_field").getAsString());

        // Save back and verify preservation
        String savedJsonStr = saveService.savePlayerState(testPlayerUuid);
        JsonObject savedJson = JsonParser.parseString(savedJsonStr).getAsJsonObject();

        assertEquals(3, savedJson.get("schema_version").getAsInt());
        assertEquals(12, savedJson.get("level").getAsInt());
        assertEquals(3400, savedJson.get("shadow_xp").getAsInt());
        assertEquals("E", savedJson.get("rank").getAsString());
        assertEquals(10, savedJson.get("strength").getAsInt());
        assertEquals(55, savedJson.get("stat_points").getAsInt());
        assertEquals("some_value", savedJson.get("custom_legacy_field").getAsString());
    }

    @Test
    public void testWorldMigrationAndLegacyPreservation() throws Exception {
        String v1Json = readFixture("world_v1.json");
        saveService.loadWorldState(v1Json);

        UmbraWorldState state = saveService.getWorldState();
        assertEquals(2, state.getSchemaVersion());
        assertEquals(3, state.getActiveStratum());
        assertTrue(state.getClearedGates().isEmpty());
        assertTrue(state.getLegacyFields().containsKey("unknown_stratum_mutator"));
        assertEquals("hardcore_only", state.getLegacyFields().get("unknown_stratum_mutator").getAsString());

        // Modify and save back
        state.getClearedGates().add("gate#123");
        String savedJsonStr = saveService.saveWorldState();
        JsonObject savedJson = JsonParser.parseString(savedJsonStr).getAsJsonObject();

        assertEquals(2, savedJson.get("schema_version").getAsInt());
        assertEquals(3, savedJson.get("active_stratum").getAsInt());
        assertEquals("hardcore_only", savedJson.get("unknown_stratum_mutator").getAsString());

        JsonArray gates = savedJson.getAsJsonArray("cleared_gates");
        assertEquals(1, gates.size());
        assertEquals("gate#123", gates.get(0).getAsString());
    }

    @Test
    public void testHighLevelRankInference() throws Exception {
        JsonObject data = new JsonObject();
        data.addProperty("schema_version", 1);
        data.addProperty("level", 55);
        data.addProperty("xp", 12000);

        saveService.loadPlayerState(testPlayerUuid, data.toString());
        UmbraPlayerState state = saveService.getOrCreatePlayerState(testPlayerUuid);

        assertEquals(3, state.getSchemaVersion());
        assertEquals(55, state.getLevel());
        assertEquals(12000, state.getShadowXp());
        assertEquals("S", state.getRank());
    }

    @Test
    public void testSaveServiceFileIO() throws Exception {
        Path tempDir = Files.createTempDirectory("umbra_state_test");
        try {
            // Initial load of non-existent world/player should succeed with defaults
            saveService.onServerStart(tempDir);
            assertEquals(1, saveService.getWorldState().getSchemaVersion()); // Default constructor is version 1

            saveService.onPlayerJoin(testPlayerUuid, tempDir);
            UmbraPlayerState playerState = saveService.getOrCreatePlayerState(testPlayerUuid);
            assertEquals(3, playerState.getSchemaVersion());
            assertEquals(1, playerState.getLevel());

            // Modify state
            saveService.getWorldState().setSchemaVersion(2);
            saveService.getWorldState().setActiveStratum(5);
            playerState.setSchemaVersion(3);
            playerState.setLevel(25);
            playerState.setShadowXp(500);
            playerState.setRank("D");

            // Trigger save
            saveService.onWorldSave(tempDir);

            // Verify files created
            Path worldFile = tempDir.resolve("umbra/world_state.json");
            Path playerFile = tempDir.resolve("umbra/players/" + testPlayerUuid.toString() + ".json");
            assertTrue(Files.exists(worldFile));
            assertTrue(Files.exists(playerFile));

            // Load on new service instance
            StateSaveServiceImpl newService = new StateSaveServiceImpl();
            newService.onServerStart(tempDir);
            assertEquals(2, newService.getWorldState().getSchemaVersion());
            assertEquals(5, newService.getWorldState().getActiveStratum());

            newService.onPlayerJoin(testPlayerUuid, tempDir);
            UmbraPlayerState newPlayerState = newService.getOrCreatePlayerState(testPlayerUuid);
            assertEquals(3, newPlayerState.getSchemaVersion());
            assertEquals(25, newPlayerState.getLevel());
            assertEquals(500, newPlayerState.getShadowXp());
            assertEquals("D", newPlayerState.getRank());

            // Player leave triggers save and cache eviction
            newService.onPlayerLeave(testPlayerUuid, tempDir);
            // Verify cache eviction by checking if a default one is now created on get
            assertNotSame(newPlayerState, newService.getOrCreatePlayerState(testPlayerUuid));

        } finally {
            // Clean up tempDir
            try (var walk = Files.walk(tempDir)) {
                walk.sorted((a, b) -> b.compareTo(a))
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException ignored) {}
                    });
            }
        }
    }

    @Test
    public void testUmbraPlayerStatePayloadRecord() {
        dev.umbra.core.contract.state.UmbraPlayerStatePayload payload =
            new dev.umbra.core.contract.state.UmbraPlayerStatePayload(5, 1200, "D", 10, 10, 10, 10, 10, 20, 0, false, 0L);
        assertEquals(5, payload.level());
        assertEquals(1200, payload.shadowXp());
        assertEquals("D", payload.rank());
        assertEquals(10, payload.strength());
        assertEquals(10, payload.agility());
        assertEquals(10, payload.vitality());
        assertEquals(10, payload.intelligence());
        assertEquals(10, payload.perception());
        assertEquals(20, payload.statPoints());
        assertEquals(0, payload.essence());
        assertFalse(payload.jobChanged());
        assertEquals(0L, payload.lastRespecTime());
        assertNotNull(dev.umbra.core.contract.state.UmbraPlayerStatePayload.TYPE);
        assertNotNull(dev.umbra.core.contract.state.UmbraPlayerStatePayload.CODEC);
    }
}

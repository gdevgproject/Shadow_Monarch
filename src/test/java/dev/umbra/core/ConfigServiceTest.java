package dev.umbra.core;

import dev.umbra.core.impl.config.UmbraConfigServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

public class ConfigServiceTest {

    @Test
    public void testDefaults(@TempDir Path tempDir) {
        Path configPath = tempDir.resolve("umbra.json");
        UmbraConfigServiceImpl service = new UmbraConfigServiceImpl(configPath);

        assertNotNull(service.getPlayerConfig());
        assertNotNull(service.getServerConfig());
        assertNotNull(service.getDevConfig());

        assertEquals("NORMAL", service.getPlayerConfig().getDifficulty());
        assertTrue(service.getPlayerConfig().isAdaptive());
        assertTrue(service.getPlayerConfig().isEffectsEnabled());

        assertEquals(1.0, service.getServerConfig().getGateFrequencyMultiplier());
        assertEquals(12, service.getServerConfig().getMaxSummonLimit());

        assertTrue(service.getDevConfig().isDebugOverlayEnabled());
        assertFalse(service.getDevConfig().isProfilingEnabled());
    }

    @Test
    public void testSaveAndLoad(@TempDir Path tempDir) throws IOException {
        Path configPath = tempDir.resolve("umbra_save.json");
        UmbraConfigServiceImpl service = new UmbraConfigServiceImpl(configPath);

        // Modify values before saving
        service.getPlayerConfig().setDifficulty("HARD");
        service.getPlayerConfig().setAdaptive(false);
        service.getServerConfig().setMaxSummonLimit(20);
        service.getServerConfig().setGateFrequencyMultiplier(2.5);
        service.getDevConfig().setDebugOverlayEnabled(false);

        service.save();
        assertTrue(Files.exists(configPath));

        // Create a new service instance to load from the saved path
        UmbraConfigServiceImpl serviceLoaded = new UmbraConfigServiceImpl(configPath);
        serviceLoaded.load();

        assertEquals("HARD", serviceLoaded.getPlayerConfig().getDifficulty());
        assertFalse(serviceLoaded.getPlayerConfig().isAdaptive());
        assertEquals(20, serviceLoaded.getServerConfig().getMaxSummonLimit());
        assertEquals(2.5, serviceLoaded.getServerConfig().getGateFrequencyMultiplier());
        assertFalse(serviceLoaded.getDevConfig().isDebugOverlayEnabled());
    }

    @Test
    public void testInvalidJsonRecovery(@TempDir Path tempDir) throws IOException {
        Path configPath = tempDir.resolve("umbra_corrupt.json");

        // Write corrupt JSON
        Files.writeString(configPath, "{ \"player\": { \"difficulty\": \"EXTREME\" } ,,, }");

        UmbraConfigServiceImpl service = new UmbraConfigServiceImpl(configPath);
        service.load();

        // Should fallback to default because of the corrupt JSON structure
        assertEquals("NORMAL", service.getPlayerConfig().getDifficulty());
        assertEquals(12, service.getServerConfig().getMaxSummonLimit());
    }

    @Test
    public void testDynamicReload(@TempDir Path tempDir) throws IOException {
        Path configPath = tempDir.resolve("umbra_reload.json");
        UmbraConfigServiceImpl service = new UmbraConfigServiceImpl(configPath);
        service.save(); // generates defaults

        service.getPlayerConfig().setDifficulty("EASY");
        // Reload from path should reset back to file's value (which is default "NORMAL")
        service.reload();
        assertEquals("NORMAL", service.getPlayerConfig().getDifficulty());
    }
}

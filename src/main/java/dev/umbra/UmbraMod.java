package dev.umbra;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Common bootstrap entrypoint. Gameplay modules are intentionally absent in M0-01.
 */
public final class UmbraMod implements ModInitializer {
    public static final String MOD_ID = "umbra";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("UMBRA bootstrap initialized");
    }
}

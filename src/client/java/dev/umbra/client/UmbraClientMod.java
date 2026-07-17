package dev.umbra.client;

import dev.umbra.UmbraMod;
import net.fabricmc.api.ClientModInitializer;

/**
 * Client-only bootstrap entrypoint; it contains no gameplay authority.
 */
public final class UmbraClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        UmbraMod.LOGGER.info("UMBRA client bootstrap initialized");
    }
}

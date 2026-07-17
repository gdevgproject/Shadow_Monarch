package dev.umbra.client;

import dev.umbra.UmbraMod;
import dev.umbra.core.contract.config.UmbraConfigService;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.resources.Identifier;

/**
 * Client-only bootstrap entrypoint; it contains no gameplay authority.
 */
public final class UmbraClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        UmbraMod.LOGGER.info("UMBRA client bootstrap initialized");

        // Retrieve UmbraConfigService from the shared registry
        UmbraConfigService configService = UmbraMod.getServiceRegistry()
            .locate(UmbraConfigService.class)
            .orElseThrow(() -> new IllegalStateException("UmbraConfigService not registered in ServiceRegistry"));

        // Register client packet receiver for player state sync
        net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.registerGlobalReceiver(
            dev.umbra.core.contract.state.UmbraPlayerStatePayload.TYPE,
            (payload, context) -> {
                context.client().execute(() -> {
                    ClientPlayerStateTracker.update(payload.level(), payload.shadowXp(), payload.rank());
                });
            }
        );

        // Register the debug overlay HUD element
        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("umbra", "debug_overlay"), new UmbraDebugOverlay(configService));
    }
}

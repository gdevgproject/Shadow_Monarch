package dev.umbra.client;

import dev.umbra.UmbraMod;
import dev.umbra.core.contract.config.UmbraConfigService;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.resources.Identifier;

/**
 * Client-only bootstrap entrypoint; it contains no gameplay authority.
 */
public final class UmbraClientMod implements ClientModInitializer {
    private static KeyMapping statsKeyBinding;

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
                    ClientPlayerStateTracker.update(
                        payload.level(),
                        payload.shadowXp(),
                        payload.rank(),
                        payload.strength(),
                        payload.agility(),
                        payload.vitality(),
                        payload.intelligence(),
                        payload.perception(),
                        payload.statPoints(),
                        payload.essence(),
                        payload.jobChanged(),
                        payload.lastRespecTime()
                    );
                });
            }
        );

        // Register key binding for stats screen
        statsKeyBinding = KeyMappingHelper.registerKeyMapping(new KeyMapping(
            "key.umbra.stats",
            InputConstants.Type.KEYSYM,
            org.lwjgl.glfw.GLFW.GLFW_KEY_K,
            KeyMapping.Category.MISC
        ));

        // Key bind listener
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (statsKeyBinding.consumeClick()) {
                if (client.player != null) {
                    client.setScreenAndShow(new UmbraStatsScreen());
                }
            }
        });

        // Register the debug overlay HUD element
        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("umbra", "debug_overlay"), new UmbraDebugOverlay(configService));
    }
}

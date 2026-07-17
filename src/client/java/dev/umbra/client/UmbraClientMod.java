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
import dev.umbra.core.contract.combat.UmbraCombatStatePayload;
import dev.umbra.core.contract.combat.DodgeDirection;
import dev.umbra.core.contract.combat.UmbraDodgeIntentPayload;
import dev.umbra.core.contract.combat.UmbraDodgeStatePayload;
import net.minecraft.network.chat.Component;

/**
 * Client-only bootstrap entrypoint; it contains no gameplay authority.
 */
public final class UmbraClientMod implements ClientModInitializer {
    private static KeyMapping statsKeyBinding;
    private static KeyMapping dodgeKeyBinding;
    private static boolean dodgeBindingChecked;

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
                        payload.lastRespecTime(),
                        payload.currentMana(),
                        payload.currentFocus(),
                        payload.fatigue()
                    );
                    ClientDodgeStateTracker.update(payload.currentMana(), payload.currentFocus(), payload.fatigue(), 0, false);
                });
            }
        );

        // Register client packet receiver for combat state sync
        net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.registerGlobalReceiver(
            UmbraCombatStatePayload.TYPE,
            (payload, context) -> {
                context.client().execute(() -> {
                    ClientCombatStateTracker.update(payload.inCombatStance(), payload.comboCount());
                });
            }
        );

        net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.registerGlobalReceiver(
            UmbraDodgeStatePayload.TYPE,
            (payload, context) -> context.client().execute(() ->
                ClientDodgeStateTracker.update(
                    payload.mana(), payload.focus(), payload.fatigue(), payload.dodgeTicksRemaining(), payload.precisionDodge()
                )
            )
        );

        // Reset client combat state on disconnect
        net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            ClientCombatStateTracker.update(false, 0);
            ClientDodgeStateTracker.update(101.0F, 100.0F, 0, 0, false);
        });

        // Register Combat Dummy entity renderer
        net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(
            UmbraMod.COMBAT_DUMMY,
            CombatDummyRenderer::new
        );

        // Register key binding for stats screen
        statsKeyBinding = KeyMappingHelper.registerKeyMapping(new KeyMapping(
            "key.umbra.stats",
            InputConstants.Type.KEYSYM,
            org.lwjgl.glfw.GLFW.GLFW_KEY_K,
            KeyMapping.Category.MISC
        ));

        dodgeKeyBinding = KeyMappingHelper.registerKeyMapping(new KeyMapping(
            "key.umbra.dodge",
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_R,
            KeyMapping.Category.MISC
        ));

        // Key bind listener
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            resolveDefaultDodgeBinding(client);
            while (statsKeyBinding.consumeClick()) {
                if (client.player != null) {
                    client.setScreenAndShow(new UmbraStatsScreen());
                }
            }
            while (dodgeKeyBinding.consumeClick()) {
                if (client.player != null) {
                    net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.send(
                        new UmbraDodgeIntentPayload(resolveDodgeDirection(client).ordinal())
                    );
                }
            }
        });

        // Register the debug overlay HUD element
        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("umbra", "debug_overlay"), new UmbraDebugOverlay(configService));
    }

    private static void resolveDefaultDodgeBinding(net.minecraft.client.Minecraft client) {
        if (dodgeBindingChecked || !dodgeKeyBinding.isDefault()) {
            return;
        }
        dodgeBindingChecked = true;
        InputConstants.Key[] candidates = {
            InputConstants.getKey("key.keyboard.r"),
            InputConstants.getKey("key.keyboard.c"),
            InputConstants.getKey("key.mouse.4")
        };
        for (InputConstants.Key candidate : candidates) {
            if (!isBoundByAnotherAction(client, candidate)) {
                dodgeKeyBinding.setKey(candidate);
                KeyMapping.resetMapping();
                if (!candidate.equals(InputConstants.getKey("key.keyboard.r")) && client.player != null) {
                    client.player.sendSystemMessage(Component.literal("UMBRA Dodge remapped to " + dodgeKeyBinding.getTranslatedKeyMessage().getString() + "."));
                }
                return;
            }
        }
        dodgeKeyBinding.setKey(InputConstants.UNKNOWN);
        KeyMapping.resetMapping();
        if (client.player != null) {
            client.player.sendSystemMessage(Component.literal("UMBRA Dodge is unbound: R, C, and Mouse4 conflict. Set it in Controls."));
        }
    }

    private static boolean isBoundByAnotherAction(net.minecraft.client.Minecraft client, InputConstants.Key candidate) {
        for (KeyMapping mapping : client.options.keyMappings) {
            if (mapping != dodgeKeyBinding && mapping.matches(candidate)) {
                return true;
            }
        }
        return false;
    }

    private static DodgeDirection resolveDodgeDirection(net.minecraft.client.Minecraft client) {
        int forward = (client.options.keyUp.isDown() ? 1 : 0) - (client.options.keyDown.isDown() ? 1 : 0);
        int strafe = (client.options.keyRight.isDown() ? 1 : 0) - (client.options.keyLeft.isDown() ? 1 : 0);
        return DodgeDirection.fromMovementAxes(forward, strafe);
    }
}

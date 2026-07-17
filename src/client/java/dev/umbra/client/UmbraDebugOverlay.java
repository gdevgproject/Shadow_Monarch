package dev.umbra.client;

import dev.umbra.core.contract.config.UmbraConfigService;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.DeltaTracker;

/**
 * Client-only debug overlay displaying safe states only.
 */
public final class UmbraDebugOverlay implements HudElement {
    private final UmbraConfigService configService;

    public UmbraDebugOverlay(UmbraConfigService configService) {
        this.configService = configService;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor drawContext, DeltaTracker deltaTracker) {
        if (!configService.getDevConfig().isDebugOverlayEnabled()) {
            return;
        }

        Minecraft client = Minecraft.getInstance();
        if (client == null || client.gui.hud.isHidden()) {
            return;
        }

        Font font = client.font;
        if (font == null) {
            return;
        }

        int y = 5;
        // Render safe states only using GuiGraphicsExtractor
        drawContext.text(font, "=== UMBRA DEBUG OVERLAY ===", 5, y, 0xFFAA00FF, false);
        y += 10;
        drawContext.text(font, "Difficulty: " + configService.getPlayerConfig().getDifficulty(), 5, y, 0xFFFFFFFF, false);
        y += 10;
        drawContext.text(font, "Adaptive: " + configService.getPlayerConfig().isAdaptive(), 5, y, 0xFFFFFFFF, false);
        y += 10;
        drawContext.text(font, "Effects Enabled: " + configService.getPlayerConfig().isEffectsEnabled(), 5, y, 0xFFFFFFFF, false);
        y += 10;

        if (configService.getDevConfig().isProfilingEnabled()) {
            drawContext.text(font, "Profiling: ACTIVE (Safe HUD: FPS=" + client.getFps() + ")", 5, y, 0xFFFF5555, false);
            y += 10;
            try {
                var registry = dev.umbra.UmbraMod.getServiceRegistry();
                if (registry != null) {
                    var scheduler = registry.locate(dev.umbra.core.contract.scheduler.TickScheduler.class).orElse(null);
                    if (scheduler != null) {
                        double avgMs = scheduler.getAverageTickDurationMs();
                        long lastNs = scheduler.getLastTickDurationNs();
                        int executed = scheduler.getLastTickExecutedCount();
                        int pending = scheduler.getPendingTasksCount();
                        drawContext.text(font, String.format("Scheduler MSPT: %.3f ms (Last: %.3f ms)", avgMs, lastNs / 1_000_000.0), 5, y, 0xFF55FF55, false);
                        y += 10;
                        drawContext.text(font, String.format("Tasks: %d run | %d pending", executed, pending), 5, y, 0xFF55FF55, false);
                        y += 10;
                    }
                }
            } catch (Exception ignored) {
            }
        }
    }
}

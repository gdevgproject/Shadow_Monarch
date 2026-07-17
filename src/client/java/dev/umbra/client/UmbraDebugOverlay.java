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
        drawContext.text(font, "Level: " + ClientPlayerStateTracker.getLevel(), 5, y, 0xFF55FFFF, false);
        y += 10;
        drawContext.text(font, "XP: " + ClientPlayerStateTracker.getShadowXp(), 5, y, 0xFF55FFFF, false);
        y += 10;
        drawContext.text(font, "Rank: " + ClientPlayerStateTracker.getRank(), 5, y, 0xFF55FFFF, false);
        y += 10;
        drawContext.text(font, String.format("Stats: STR=%d AGI=%d VIT=%d INT=%d PER=%d",
            ClientPlayerStateTracker.getStrength(),
            ClientPlayerStateTracker.getAgility(),
            ClientPlayerStateTracker.getVitality(),
            ClientPlayerStateTracker.getIntelligence(),
            ClientPlayerStateTracker.getPerception()), 5, y, 0xFF55FF55, false);
        y += 10;
        drawContext.text(font, "Free Points: " + ClientPlayerStateTracker.getStatPoints() +
            " | Essence: " + ClientPlayerStateTracker.getEssence(), 5, y, 0xFF55FF55, false);
        y += 10;
        drawContext.text(font, "Job Changed: " + ClientPlayerStateTracker.isJobChanged(), 5, y, 0xFF55FF55, false);
        y += 10;
        drawContext.text(font, "Difficulty: " + configService.getPlayerConfig().getDifficulty(), 5, y, 0xFFFFFFFF, false);
        y += 10;
        drawContext.text(font, "Adaptive: " + configService.getPlayerConfig().isAdaptive(), 5, y, 0xFFFFFFFF, false);
        y += 10;
        drawContext.text(font, "Effects Enabled: " + configService.getPlayerConfig().isEffectsEnabled(), 5, y, 0xFFFFFFFF, false);
        y += 10;

        boolean inCombat = ClientCombatStateTracker.isInCombatStance();
        int combo = ClientCombatStateTracker.getComboCount();
        String combatText = inCombat ? "Stance: IN | Combo: " + combo : "Stance: OUT | Combo: 0";
        int combatColor = inCombat ? 0xFFFF5555 : 0x88FFFFFF;
        drawContext.text(font, combatText, 5, y, combatColor, false);
        y += 10;
        drawContext.text(font, String.format("Mana: %.0f | Focus: %.0f | Fatigue: %d", ClientDodgeStateTracker.getMana(), ClientDodgeStateTracker.getFocus(), ClientDodgeStateTracker.getFatigue()), 5, y, 0xFFB388FF, false);
        y += 10;
        if (ClientDodgeStateTracker.getDodgeTicksRemaining() > 0 || ClientDodgeStateTracker.isPrecisionDodge()) {
            String dodgeText = ClientDodgeStateTracker.isPrecisionDodge() ? "Dodge: PRECISION" : "Dodge: ACTIVE";
            drawContext.text(font, dodgeText, 5, y, 0xFFB388FF, false);
            y += 10;
        }

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

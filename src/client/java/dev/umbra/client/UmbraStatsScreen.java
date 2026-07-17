package dev.umbra.client;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import dev.umbra.core.contract.state.UmbraStatsAllocatePayload;
import dev.umbra.core.contract.state.UmbraStatsRespecPayload;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public final class UmbraStatsScreen extends Screen {

    private int strAdd = 0;
    private int agiAdd = 0;
    private int vitAdd = 0;
    private int intelAdd = 0;
    private int perAdd = 0;

    private int pointsRemaining;

    // Buttons
    private Button confirmButton;
    private Button respecButton;

    // Interactive +/- buttons
    private Button strMinus, strPlus;
    private Button agiMinus, agiPlus;
    private Button vitMinus, vitPlus;
    private Button intelMinus, intelPlus;
    private Button perMinus, perPlus;

    public UmbraStatsScreen() {
        super(Component.literal("System Attributes"));
        this.pointsRemaining = ClientPlayerStateTracker.getStatPoints();
    }

    @Override
    protected void init() {
        super.init();

        int pWidth = 400;
        int pHeight = 220;
        int startX = (this.width - pWidth) / 2;
        int startY = (this.height - pHeight) / 2;

        // Reset inputs on initialization
        this.pointsRemaining = ClientPlayerStateTracker.getStatPoints();
        this.strAdd = 0;
        this.agiAdd = 0;
        this.vitAdd = 0;
        this.intelAdd = 0;
        this.perAdd = 0;

        // Position for +/- buttons
        int leftColX = startX + 165;
        int rowHeight = 22;
        int firstRowY = startY + 45;

        // STR
        strMinus = this.addRenderableWidget(Button.builder(Component.literal("-"), b -> adjustStat(0, -1)).bounds(leftColX, firstRowY, 14, 14).build());
        strPlus = this.addRenderableWidget(Button.builder(Component.literal("+"), b -> adjustStat(0, 1)).bounds(leftColX + 18, firstRowY, 14, 14).build());

        // AGI
        agiMinus = this.addRenderableWidget(Button.builder(Component.literal("-"), b -> adjustStat(1, -1)).bounds(leftColX, firstRowY + rowHeight, 14, 14).build());
        agiPlus = this.addRenderableWidget(Button.builder(Component.literal("+"), b -> adjustStat(1, 1)).bounds(leftColX + 18, firstRowY + rowHeight, 14, 14).build());

        // VIT
        vitMinus = this.addRenderableWidget(Button.builder(Component.literal("-"), b -> adjustStat(2, -1)).bounds(leftColX, firstRowY + rowHeight * 2, 14, 14).build());
        vitPlus = this.addRenderableWidget(Button.builder(Component.literal("+"), b -> adjustStat(2, 1)).bounds(leftColX + 18, firstRowY + rowHeight * 2, 14, 14).build());

        // INT
        intelMinus = this.addRenderableWidget(Button.builder(Component.literal("-"), b -> adjustStat(3, -1)).bounds(leftColX, firstRowY + rowHeight * 3, 14, 14).build());
        intelPlus = this.addRenderableWidget(Button.builder(Component.literal("+"), b -> adjustStat(3, 1)).bounds(leftColX + 18, firstRowY + rowHeight * 3, 14, 14).build());

        // PER
        perMinus = this.addRenderableWidget(Button.builder(Component.literal("-"), b -> adjustStat(4, -1)).bounds(leftColX, firstRowY + rowHeight * 4, 14, 14).build());
        perPlus = this.addRenderableWidget(Button.builder(Component.literal("+"), b -> adjustStat(4, 1)).bounds(leftColX + 18, firstRowY + rowHeight * 4, 14, 14).build());

        // Confirm Button
        confirmButton = this.addRenderableWidget(Button.builder(Component.literal("Confirm Stats"), b -> confirmAllocation())
            .bounds(startX + 20, startY + pHeight - 35, 110, 20).build());

        // Respec Button
        respecButton = this.addRenderableWidget(Button.builder(Component.literal("Reset Stats"), b -> requestRespec())
            .bounds(startX + 145, startY + pHeight - 35, 110, 20).build());

        // Close Button
        this.addRenderableWidget(Button.builder(Component.literal("Close"), b -> this.onClose())
            .bounds(startX + 270, startY + pHeight - 35, 110, 20).build());

        updateButtonStates();
    }

    private void adjustStat(int index, int amount) {
        if (amount > 0 && pointsRemaining > 0) {
            if (index == 0) strAdd++;
            else if (index == 1) agiAdd++;
            else if (index == 2) vitAdd++;
            else if (index == 3) intelAdd++;
            else if (index == 4) perAdd++;
            pointsRemaining--;
        } else if (amount < 0) {
            if (index == 0 && strAdd > 0) { strAdd--; pointsRemaining++; }
            else if (index == 1 && agiAdd > 0) { agiAdd--; pointsRemaining++; }
            else if (index == 2 && vitAdd > 0) { vitAdd--; pointsRemaining++; }
            else if (index == 3 && intelAdd > 0) { intelAdd--; pointsRemaining++; }
            else if (index == 4 && perAdd > 0) { perAdd--; pointsRemaining++; }
        }
        updateButtonStates();
    }

    private void updateButtonStates() {
        boolean hasPointsToAllocate = pointsRemaining > 0;
        strPlus.active = hasPointsToAllocate;
        agiPlus.active = hasPointsToAllocate;
        vitPlus.active = hasPointsToAllocate;
        intelPlus.active = hasPointsToAllocate;
        perPlus.active = hasPointsToAllocate;

        strMinus.active = strAdd > 0;
        agiMinus.active = agiAdd > 0;
        vitMinus.active = vitAdd > 0;
        intelMinus.active = intelAdd > 0;
        perMinus.active = perAdd > 0;

        int sum = strAdd + agiAdd + vitAdd + intelAdd + perAdd;
        confirmButton.active = sum > 0;

        // Respec constraints: Job changed, 10 essence, and cooldown
        boolean job = ClientPlayerStateTracker.isJobChanged();
        boolean essence = ClientPlayerStateTracker.getEssence() >= 10;
        long timeSinceLastRespec = 0;
        if (this.minecraft != null && this.minecraft.level != null) {
            timeSinceLastRespec = this.minecraft.level.getGameTime() - ClientPlayerStateTracker.getLastRespecTime();
        }
        boolean cooldownMet = (ClientPlayerStateTracker.getLastRespecTime() == 0) || (timeSinceLastRespec >= 72000);
        respecButton.active = job && essence && cooldownMet;
    }

    private void confirmAllocation() {
        ClientPlayNetworking.send(new UmbraStatsAllocatePayload(strAdd, agiAdd, vitAdd, intelAdd, perAdd));
        this.onClose();
    }

    private void requestRespec() {
        ClientPlayNetworking.send(new UmbraStatsRespecPayload(true));
        this.onClose();
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float delta) {
        int pWidth = 400;
        int pHeight = 220;
        int startX = (guiGraphics.guiWidth() - pWidth) / 2;
        int startY = (guiGraphics.guiHeight() - pHeight) / 2;

        // Custom premium glassmorphism background: dark body with border
        guiGraphics.fill(startX, startY, startX + pWidth, startY + pHeight, 0xD0101017);
        // Border lines
        guiGraphics.fill(startX, startY, startX + pWidth, startY + 1, 0xFF5A5A75); // Top
        guiGraphics.fill(startX, startY + pHeight - 1, startX + pWidth, startY + pHeight, 0xFF5A5A75); // Bottom
        guiGraphics.fill(startX, startY, startX + 1, startY + pHeight, 0xFF5A5A75); // Left
        guiGraphics.fill(startX + pWidth - 1, startY, startX + pWidth, startY + pHeight, 0xFF5A5A75); // Right

        // Draw title
        guiGraphics.centeredText(this.font, "§5§l★ SYSTEM ATTRIBUTES INTERFACE ★", startX + pWidth / 2, startY + 8, 0xFFD4AF37);

        // Subheader player general stats
        String headerText = String.format("Level: §b%d§r | Rank: §e%s§r | Essence: §d%d",
            ClientPlayerStateTracker.getLevel(),
            ClientPlayerStateTracker.getRank(),
            ClientPlayerStateTracker.getEssence()
        );
        guiGraphics.centeredText(this.font, headerText, startX + pWidth / 2, startY + 22, 0xFFFFFFFF);

        // Drawing Attributes
        int textX = startX + 15;
        int rowHeight = 22;
        int firstRowY = startY + 47;

        drawStatRow(guiGraphics, "STR (Strength)", ClientPlayerStateTracker.getStrength(), strAdd, textX, firstRowY);
        drawStatRow(guiGraphics, "AGI (Agility)", ClientPlayerStateTracker.getAgility(), agiAdd, textX, firstRowY + rowHeight);
        drawStatRow(guiGraphics, "VIT (Vitality)", ClientPlayerStateTracker.getVitality(), vitAdd, textX, firstRowY + rowHeight * 2);
        drawStatRow(guiGraphics, "INT (Intelligence)", ClientPlayerStateTracker.getIntelligence(), intelAdd, textX, firstRowY + rowHeight * 3);
        drawStatRow(guiGraphics, "PER (Perception)", ClientPlayerStateTracker.getPerception(), perAdd, textX, firstRowY + rowHeight * 4);

        // Points left to allocate
        String pointsText = "Unallocated Points: §6" + pointsRemaining;
        guiGraphics.text(this.font, pointsText, startX + 15, startY + pHeight - 55, 0xFFFFFFFF, false);

        // Right side: Previews of derived formulas
        int previewX = startX + 215;
        int previewY = startY + 47;

        guiGraphics.text(this.font, "§d§nDerived Stats Preview:§r", previewX, previewY - 12, 0xFFFFFFFF, false);

        int level = ClientPlayerStateTracker.getLevel();
        int baseStrength = ClientPlayerStateTracker.getStrength();
        int baseAgility = ClientPlayerStateTracker.getAgility();
        int baseVitality = ClientPlayerStateTracker.getVitality();
        int baseIntelligence = ClientPlayerStateTracker.getIntelligence();
        int basePerception = ClientPlayerStateTracker.getPerception();

        // Calculate current derived
        float currentHp = 20.0f + getEffectiveStat(baseVitality) * 6.0f + level * 2.0f;
        float currentMp = 20.0f + getEffectiveStat(baseIntelligence) * 8.0f + level * 1.0f;
        double currentDmg = getEffectiveStat(baseStrength) * 1.2;
        double currentSpeed = getEffectiveStat(baseAgility) * 0.15;
        double currentIframe = 0.25 + getEffectiveStat(baseAgility) * 0.001;
        double currentCritC = 5.0 + getEffectiveStat(basePerception) * 0.25;
        double currentCritD = 150.0 + getEffectiveStat(basePerception) * 0.5;

        // Calculate preview derived
        float previewHp = 20.0f + getEffectiveStat(baseVitality + vitAdd) * 6.0f + level * 2.0f;
        float previewMp = 20.0f + getEffectiveStat(baseIntelligence + intelAdd) * 8.0f + level * 1.0f;
        double previewDmg = getEffectiveStat(baseStrength + strAdd) * 1.2;
        double previewSpeed = getEffectiveStat(baseAgility + agiAdd) * 0.15;
        double previewIframe = 0.25 + getEffectiveStat(baseAgility + agiAdd) * 0.001;
        double previewCritC = Math.min(60.0, 5.0 + getEffectiveStat(basePerception + perAdd) * 0.25);
        double previewCritD = Math.min(250.0, 150.0 + getEffectiveStat(basePerception + perAdd) * 0.5);

        // Rendering right-hand previews with arrow updates
        drawDerivedRow(guiGraphics, "Max HP", (int)currentHp, (int)previewHp, previewX, previewY);
        drawDerivedRow(guiGraphics, "Max MP", (int)currentMp, (int)previewMp, previewX, previewY + 14);
        drawDerivedRow(guiGraphics, "Bonus Melee DMG", currentDmg, previewDmg, "+%.1f", previewX, previewY + 28);
        drawDerivedRow(guiGraphics, "Move Speed", currentSpeed, previewSpeed, "+%.2f%%", previewX, previewY + 42);
        drawDerivedRow(guiGraphics, "Dodge i-frame", currentIframe, previewIframe, "%.3fs", previewX, previewY + 56);
        drawDerivedRow(guiGraphics, "Crit Chance", currentCritC, previewCritC, "%.2f%%", previewX, previewY + 70);
        drawDerivedRow(guiGraphics, "Crit Damage", currentCritD, previewCritD, "%.1f%%", previewX, previewY + 84);

        // Call super render (renders components/widgets/buttons)
        super.extractRenderState(guiGraphics, mouseX, mouseY, delta);

        // Draw tooltips on hover
        renderHoverTooltips(guiGraphics, mouseX, mouseY, startX, startY, pWidth, pHeight);
    }

    private void drawStatRow(GuiGraphicsExtractor guiGraphics, String label, int baseVal, int addedVal, int x, int y) {
        String text = label + ": " + baseVal;
        if (addedVal > 0) {
            text += " §a(+" + addedVal + ")";
        }
        guiGraphics.text(this.font, text, x, y + 3, 0xFFFFFFFF, false);
    }

    private void drawDerivedRow(GuiGraphicsExtractor guiGraphics, String label, int current, int preview, int x, int y) {
        String text;
        if (current == preview) {
            text = String.format("%s: §7%d", label, current);
        } else {
            text = String.format("%s: §7%d §r➡ §e%d", label, current, preview);
        }
        guiGraphics.text(this.font, text, x, y, 0xFFCCCCCC, false);
    }

    private void drawDerivedRow(GuiGraphicsExtractor guiGraphics, String label, double current, double preview, String format, int x, int y) {
        String text;
        if (Math.abs(current - preview) < 0.0001) {
            text = String.format("%s: §7" + format, label, current);
        } else {
            text = String.format("%s: §7" + format + " §r➡ §e" + format, label, current, preview);
        }
        guiGraphics.text(this.font, text, x, y, 0xFFCCCCCC, false);
    }

    private void renderHoverTooltips(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, int startX, int startY, int pWidth, int pHeight) {
        // Reset stats button tooltip
        if (respecButton.isMouseOver(mouseX, mouseY)) {
            List<Component> tooltip = new ArrayList<>();
            tooltip.add(Component.literal("§dReset All Attributes"));
            tooltip.add(Component.literal("§7Resets stats back to 10 and returns points."));
            tooltip.add(Component.literal("§8Cost: §d10 Essence"));

            boolean job = ClientPlayerStateTracker.isJobChanged();
            boolean essence = ClientPlayerStateTracker.getEssence() >= 10;
            long timeSinceLastRespec = 0;
            if (this.minecraft != null && this.minecraft.level != null) {
                timeSinceLastRespec = this.minecraft.level.getGameTime() - ClientPlayerStateTracker.getLastRespecTime();
            }
            boolean cooldownMet = (ClientPlayerStateTracker.getLastRespecTime() == 0) || (timeSinceLastRespec >= 72000);

            if (!job) {
                tooltip.add(Component.literal("§cRequires Job Changed status."));
            }
            if (!essence) {
                tooltip.add(Component.literal("§cInsufficient Essence (need 10)."));
            }
            if (!cooldownMet) {
                long secondsLeft = (72000 - timeSinceLastRespec) / 20;
                tooltip.add(Component.literal(String.format("§cOn Cooldown! Wait %d min.", secondsLeft / 60)));
            }

            guiGraphics.setComponentTooltipForNextFrame(this.font, tooltip, mouseX, mouseY);
        }
    }

    private int getEffectiveStat(int rawValue) {
        if (rawValue <= 100) {
            return rawValue;
        }
        return 100 + (int) Math.floor(Math.pow(rawValue - 100, 0.75));
    }

    @Override
    public boolean keyPressed(net.minecraft.client.input.KeyEvent event) {
        if (event.isEscape() || event.key() == GLFW.GLFW_KEY_K) {
            this.onClose();
            return true;
        }
        return super.keyPressed(event);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}

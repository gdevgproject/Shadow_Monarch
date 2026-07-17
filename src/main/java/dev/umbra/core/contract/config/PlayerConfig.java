package dev.umbra.core.contract.config;

/**
 * Player-scoped configuration settings.
 */
public final class PlayerConfig {
    private String difficulty = "NORMAL";
    private boolean adaptive = true;
    private boolean effectsEnabled = true;

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public boolean isAdaptive() {
        return adaptive;
    }

    public void setAdaptive(boolean adaptive) {
        this.adaptive = adaptive;
    }

    public boolean isEffectsEnabled() {
        return effectsEnabled;
    }

    public void setEffectsEnabled(boolean effectsEnabled) {
        this.effectsEnabled = effectsEnabled;
    }
}

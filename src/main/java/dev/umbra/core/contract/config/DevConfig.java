package dev.umbra.core.contract.config;

/**
 * Developer/debug-scoped configuration settings.
 */
public final class DevConfig {
    private boolean debugOverlayEnabled = true;
    private boolean profilingEnabled = false;

    public boolean isDebugOverlayEnabled() {
        return debugOverlayEnabled;
    }

    public void setDebugOverlayEnabled(boolean debugOverlayEnabled) {
        this.debugOverlayEnabled = debugOverlayEnabled;
    }

    public boolean isProfilingEnabled() {
        return profilingEnabled;
    }

    public void setProfilingEnabled(boolean profilingEnabled) {
        this.profilingEnabled = profilingEnabled;
    }
}

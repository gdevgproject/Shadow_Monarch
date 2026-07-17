package dev.umbra.client;

/**
 * Tracks the local player's combat state replicated from the server.
 * Used on the client side for HUD and rendering.
 */
public final class ClientCombatStateTracker {
    private static boolean inCombatStance = false;
    private static int comboCount = 0;

    private ClientCombatStateTracker() {}

    public static synchronized void update(boolean active, int combo) {
        inCombatStance = active;
        comboCount = combo;
    }

    public static synchronized boolean isInCombatStance() {
        return inCombatStance;
    }

    public static synchronized int getComboCount() {
        return comboCount;
    }
}

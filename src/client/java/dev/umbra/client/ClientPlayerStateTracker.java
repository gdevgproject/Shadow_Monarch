package dev.umbra.client;

/**
 * Tracks the local player's state replicated from the server.
 * Used on the client side for HUD and rendering.
 */
public final class ClientPlayerStateTracker {
    private static int level = 1;
    private static int shadowXp = 0;
    private static String rank = "E";

    private ClientPlayerStateTracker() {}

    public static synchronized void update(int newLevel, int newShadowXp, String newRank) {
        level = newLevel;
        shadowXp = newShadowXp;
        rank = newRank;
    }

    public static synchronized int getLevel() {
        return level;
    }

    public static synchronized int getShadowXp() {
        return shadowXp;
    }

    public static synchronized String getRank() {
        return rank;
    }
}

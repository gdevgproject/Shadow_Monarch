package dev.umbra.client;

/** Client presentation cache for server-authoritative dodge/resource state. */
public final class ClientDodgeStateTracker {
    private static float mana = 101.0F;
    private static float focus = 100.0F;
    private static int fatigue;
    private static int dodgeTicksRemaining;
    private static boolean precisionDodge;

    private ClientDodgeStateTracker() {}

    public static synchronized void update(float newMana, float newFocus, int newFatigue, int newDodgeTicksRemaining, boolean newPrecisionDodge) {
        mana = newMana;
        focus = newFocus;
        fatigue = newFatigue;
        dodgeTicksRemaining = newDodgeTicksRemaining;
        precisionDodge = newPrecisionDodge;
    }

    public static synchronized float getMana() { return mana; }
    public static synchronized float getFocus() { return focus; }
    public static synchronized int getFatigue() { return fatigue; }
    public static synchronized int getDodgeTicksRemaining() { return dodgeTicksRemaining; }
    public static synchronized boolean isPrecisionDodge() { return precisionDodge; }
}

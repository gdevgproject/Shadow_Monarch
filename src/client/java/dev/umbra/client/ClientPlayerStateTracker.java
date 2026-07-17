package dev.umbra.client;

/**
 * Tracks the local player's state replicated from the server.
 * Used on the client side for HUD and rendering.
 */
public final class ClientPlayerStateTracker {
    private static int level = 1;
    private static int shadowXp = 0;
    private static String rank = "E";
    private static int strength = 10;
    private static int agility = 10;
    private static int vitality = 10;
    private static int intelligence = 10;
    private static int perception = 10;
    private static int statPoints = 0;
    private static int essence = 0;
    private static boolean jobChanged = false;
    private static long lastRespecTime = 0;

    private ClientPlayerStateTracker() {}

    public static synchronized void update(
        int newLevel, int newShadowXp, String newRank,
        int newStr, int newAgi, int newVit, int newInt, int newPer,
        int newStatPoints, int newEssence, boolean newJobChanged, long newLastRespecTime
    ) {
        level = newLevel;
        shadowXp = newShadowXp;
        rank = newRank;
        strength = newStr;
        agility = newAgi;
        vitality = newVit;
        intelligence = newInt;
        perception = newPer;
        statPoints = newStatPoints;
        essence = newEssence;
        jobChanged = newJobChanged;
        lastRespecTime = newLastRespecTime;
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

    public static synchronized int getStrength() {
        return strength;
    }

    public static synchronized int getAgility() {
        return agility;
    }

    public static synchronized int getVitality() {
        return vitality;
    }

    public static synchronized int getIntelligence() {
        return intelligence;
    }

    public static synchronized int getPerception() {
        return perception;
    }

    public static synchronized int getStatPoints() {
        return statPoints;
    }

    public static synchronized int getEssence() {
        return essence;
    }

    public static synchronized boolean isJobChanged() {
        return jobChanged;
    }

    public static synchronized long getLastRespecTime() {
        return lastRespecTime;
    }
}

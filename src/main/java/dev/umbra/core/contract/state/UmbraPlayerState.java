package dev.umbra.core.contract.state;

import com.google.gson.JsonElement;
import dev.umbra.core.contract.quest.ActiveQuestEntry;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents the persistent state of a player (schema v5 adds quest progress).
 */
public final class UmbraPlayerState {
    private int schemaVersion;
    private int level;
    private int shadowXp;
    private String rank;
    private int strength;
    private int agility;
    private int vitality;
    private int intelligence;
    private int perception;
    private int statPoints;
    private int essence;
    private boolean jobChanged;
    private long lastRespecTime;
    private double currentMana;
    private double currentFocus;
    private int fatigue;
    /** questId → ActiveQuestEntry; ordered for stable serialisation. */
    private final Map<String, ActiveQuestEntry> activeQuests = new LinkedHashMap<>();
    /** Ids of quests already claimed; ordered for stable serialisation. */
    private final Set<String> completedQuestIds = new LinkedHashSet<>();
    private final Map<String, JsonElement> legacyFields = new HashMap<>();

    public UmbraPlayerState() {
        this.schemaVersion = 5;
        this.level = 1;
        this.shadowXp = 0;
        this.rank = "E";
        this.strength = 10;
        this.agility = 10;
        this.vitality = 10;
        this.intelligence = 10;
        this.perception = 10;
        this.statPoints = 0;
        this.essence = 0;
        this.jobChanged = false;
        this.lastRespecTime = 0;
        this.currentMana = maximumManaFor(level, intelligence);
        this.currentFocus = 100.0;
        this.fatigue = 0;
    }

    public int getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(int schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getShadowXp() {
        return shadowXp;
    }

    public void setShadowXp(int shadowXp) {
        this.shadowXp = shadowXp;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public int getVitality() {
        return vitality;
    }

    public void setVitality(int vitality) {
        this.vitality = vitality;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public int getPerception() {
        return perception;
    }

    public void setPerception(int perception) {
        this.perception = perception;
    }

    public int getStatPoints() {
        return statPoints;
    }

    public void setStatPoints(int statPoints) {
        this.statPoints = statPoints;
    }

    public int getEssence() {
        return essence;
    }

    public void setEssence(int essence) {
        this.essence = essence;
    }

    public boolean isJobChanged() {
        return jobChanged;
    }

    public void setJobChanged(boolean jobChanged) {
        this.jobChanged = jobChanged;
    }

    public long getLastRespecTime() {
        return lastRespecTime;
    }

    public void setLastRespecTime(long lastRespecTime) {
        this.lastRespecTime = lastRespecTime;
    }

    public double getCurrentMana() {
        return currentMana;
    }

    public void setCurrentMana(double currentMana) {
        this.currentMana = currentMana;
    }

    public double getCurrentFocus() {
        return currentFocus;
    }

    public void setCurrentFocus(double currentFocus) {
        this.currentFocus = currentFocus;
    }

    public int getFatigue() {
        return fatigue;
    }

    public void setFatigue(int fatigue) {
        this.fatigue = fatigue;
    }

    private static double maximumManaFor(int level, int intelligence) {
        return 20.0 + intelligence * 8.0 + level;
    }

    /** Returns the live (mutable) map of active quest entries keyed by questId. */
    public Map<String, ActiveQuestEntry> getActiveQuests() {
        return activeQuests;
    }

    /** Returns the live (mutable) set of completed quest ids. */
    public Set<String> getCompletedQuestIds() {
        return completedQuestIds;
    }

    public Map<String, JsonElement> getLegacyFields() {
        return legacyFields;
    }
}

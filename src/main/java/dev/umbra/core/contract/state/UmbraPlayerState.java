package dev.umbra.core.contract.state;

import com.google.gson.JsonElement;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the persistent state of a player.
 */
public final class UmbraPlayerState {
    private int schemaVersion;
    private int level;
    private int shadowXp;
    private String rank;
    private final Map<String, JsonElement> legacyFields = new HashMap<>();

    public UmbraPlayerState() {
        this.schemaVersion = 1;
        this.level = 1;
        this.shadowXp = 0;
        this.rank = "E";
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

    public Map<String, JsonElement> getLegacyFields() {
        return legacyFields;
    }
}

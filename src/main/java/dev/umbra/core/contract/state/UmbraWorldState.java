package dev.umbra.core.contract.state;

import com.google.gson.JsonElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the persistent state of the world.
 */
public final class UmbraWorldState {
    private int schemaVersion;
    private int activeStratum;
    private final List<String> clearedGates = new ArrayList<>();
    private final Map<String, JsonElement> legacyFields = new HashMap<>();

    public UmbraWorldState() {
        this.schemaVersion = 1;
        this.activeStratum = 0;
    }

    public int getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(int schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public int getActiveStratum() {
        return activeStratum;
    }

    public void setActiveStratum(int activeStratum) {
        this.activeStratum = activeStratum;
    }

    public List<String> getClearedGates() {
        return clearedGates;
    }

    public Map<String, JsonElement> getLegacyFields() {
        return legacyFields;
    }
}

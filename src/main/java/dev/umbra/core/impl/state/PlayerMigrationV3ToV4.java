package dev.umbra.core.impl.state;

import com.google.gson.JsonObject;

/** Adds persistent combat resources introduced by M1-05. */
public final class PlayerMigrationV3ToV4 implements StateMigration {
    @Override public int getSourceVersion() { return 3; }
    @Override public int getTargetVersion() { return 4; }

    @Override
    public JsonObject migrate(JsonObject data) {
        int level = data.has("level") ? data.get("level").getAsInt() : 1;
        int intelligence = data.has("intelligence") ? data.get("intelligence").getAsInt() : 10;
        if (!data.has("current_mana")) data.addProperty("current_mana", 20.0 + effectiveStat(intelligence) * 8.0 + level);
        if (!data.has("current_focus")) data.addProperty("current_focus", 100.0);
        if (!data.has("fatigue")) data.addProperty("fatigue", 0);
        data.addProperty("schema_version", 4);
        return data;
    }

    private int effectiveStat(int rawValue) {
        if (rawValue <= 100) return rawValue;
        return 100 + (int) Math.floor(Math.pow(rawValue - 100, 0.75));
    }
}

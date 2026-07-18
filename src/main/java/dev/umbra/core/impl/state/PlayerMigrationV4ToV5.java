package dev.umbra.core.impl.state;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Migration: player state schema v4 → v5.
 *
 * <p>v5 adds two new JSON keys:
 * <ul>
 *   <li>{@code active_quests}  — array of {@code {quest_id, progress}} objects (default: empty array)</li>
 *   <li>{@code completed_quest_ids} — array of strings (default: empty array)</li>
 * </ul>
 *
 * <p>No v4 fields are removed or renamed; the migration is strictly additive.
 */
final class PlayerMigrationV4ToV5 implements StateMigration {

    @Override
    public int getSourceVersion() {
        return 4;
    }

    @Override
    public int getTargetVersion() {
        return 5;
    }

    @Override
    public JsonObject migrate(JsonObject source) {
        JsonObject out = source.deepCopy();
        out.addProperty("schema_version", 5);
        if (!out.has("active_quests")) {
            out.add("active_quests", new JsonArray());
        }
        if (!out.has("completed_quest_ids")) {
            out.add("completed_quest_ids", new JsonArray());
        }
        return out;
    }
}

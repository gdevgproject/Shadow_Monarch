package dev.umbra.core.impl.state;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Migration step that upgrades WorldState from v1 to v2.
 * - Ensures 'cleared_gates' array is present.
 * - Sets 'schema_version' to 2.
 */
public final class WorldMigrationV1ToV2 implements StateMigration {
    @Override
    public int getSourceVersion() {
        return 1;
    }

    @Override
    public int getTargetVersion() {
        return 2;
    }

    @Override
    public JsonObject migrate(JsonObject data) {
        // Ensure cleared_gates is present
        if (!data.has("cleared_gates")) {
            data.add("cleared_gates", new JsonArray());
        }

        // Update version
        data.addProperty("schema_version", 2);

        return data;
    }
}

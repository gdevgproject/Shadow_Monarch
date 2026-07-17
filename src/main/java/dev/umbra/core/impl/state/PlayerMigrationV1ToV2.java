package dev.umbra.core.impl.state;

import com.google.gson.JsonObject;

/**
 * Migration step that upgrades PlayerState from v1 to v2.
 * - Renames 'xp' to 'shadow_xp'.
 * - Infers 'rank' based on 'level'.
 * - Sets 'schema_version' to 2.
 */
public final class PlayerMigrationV1ToV2 implements StateMigration {
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
        // Rename xp to shadow_xp
        if (data.has("xp")) {
            int xpVal = data.get("xp").getAsInt();
            data.addProperty("shadow_xp", xpVal);
            data.remove("xp");
        }

        // Infer rank based on level
        int level = 1;
        if (data.has("level")) {
            level = data.get("level").getAsInt();
        } else {
            data.addProperty("level", level);
        }

        String rank = (level >= 50) ? "S" : "E";
        data.addProperty("rank", rank);

        // Update version
        data.addProperty("schema_version", 2);

        return data;
    }
}

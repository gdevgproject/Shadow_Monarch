package dev.umbra.core.impl.state;

import com.google.gson.JsonObject;

/**
 * Migration step that upgrades PlayerState from v2 to v3.
 * - Adds strength, agility, vitality, intelligence, perception initialized to 10.
 * - Computes free stat_points as (level - 1) * 5.
 * - Sets essence to 0, job_changed to false, last_respec_time to 0.
 * - Sets 'schema_version' to 3.
 */
public final class PlayerMigrationV2ToV3 implements StateMigration {
    @Override
    public int getSourceVersion() {
        return 2;
    }

    @Override
    public int getTargetVersion() {
        return 3;
    }

    @Override
    public JsonObject migrate(JsonObject data) {
        int level = 1;
        if (data.has("level")) {
            level = data.get("level").getAsInt();
        }

        if (!data.has("strength")) data.addProperty("strength", 10);
        if (!data.has("agility")) data.addProperty("agility", 10);
        if (!data.has("vitality")) data.addProperty("vitality", 10);
        if (!data.has("intelligence")) data.addProperty("intelligence", 10);
        if (!data.has("perception")) data.addProperty("perception", 10);

        int expectedPoints = (level - 1) * 5;
        if (!data.has("stat_points")) data.addProperty("stat_points", expectedPoints);
        if (!data.has("essence")) data.addProperty("essence", 0);
        if (!data.has("job_changed")) data.addProperty("job_changed", false);
        if (!data.has("last_respec_time")) data.addProperty("last_respec_time", 0L);

        // Update version
        data.addProperty("schema_version", 3);

        return data;
    }
}

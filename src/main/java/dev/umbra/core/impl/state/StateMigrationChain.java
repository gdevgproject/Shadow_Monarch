package dev.umbra.core.impl.state;

import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Executes a sequential chain of migrations on raw JSON state.
 */
public final class StateMigrationChain {
    private final List<StateMigration> migrations = new ArrayList<>();

    public void registerMigration(StateMigration migration) {
        migrations.add(migration);
        // Sort migrations by source version to ensure they are sequential
        migrations.sort(Comparator.comparingInt(StateMigration::getSourceVersion));
    }

    /**
     * Migrates the given JSON state up to the target version.
     */
    public JsonObject migrate(JsonObject data, int targetVersion) {
        if (data == null) {
            data = new JsonObject();
        }

        int currentVersion = 1;
        if (data.has("schema_version")) {
            currentVersion = data.get("schema_version").getAsInt();
        } else {
            // Default to version 1 if missing
            data.addProperty("schema_version", 1);
        }

        if (currentVersion > targetVersion) {
            throw new IllegalStateException("Downgrades are not supported: state version (" +
                    currentVersion + ") is higher than system version (" + targetVersion + ")");
        }

        while (currentVersion < targetVersion) {
            StateMigration migration = findMigration(currentVersion, currentVersion + 1);
            if (migration == null) {
                throw new IllegalStateException("Missing migration path from version " +
                        currentVersion + " to " + (currentVersion + 1));
            }
            data = migration.migrate(data);
            currentVersion = data.get("schema_version").getAsInt();
        }

        return data;
    }

    private StateMigration findMigration(int source, int target) {
        for (StateMigration m : migrations) {
            if (m.getSourceVersion() == source && m.getTargetVersion() == target) {
                return m;
            }
        }
        return null;
    }
}

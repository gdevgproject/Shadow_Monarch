package dev.umbra.core.impl.state;

import com.google.gson.JsonObject;

/**
 * Interface representing a single step in a migration chain.
 */
public interface StateMigration {
    /**
     * The schema version this migration starts from.
     */
    int getSourceVersion();

    /**
     * The schema version this migration upgrades to.
     */
    int getTargetVersion();

    /**
     * Applies the migration logic to the raw JSON state data.
     * Returns a new or modified JsonObject.
     */
    JsonObject migrate(JsonObject data);
}

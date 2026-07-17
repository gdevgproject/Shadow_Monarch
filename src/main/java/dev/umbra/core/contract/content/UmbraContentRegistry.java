package dev.umbra.core.contract.content;

import java.util.Collection;
import java.util.Optional;

/**
 * Service contract for content lookups.
 */
public interface UmbraContentRegistry {
    /**
     * Look up an enemy definition by its ID.
     */
    Optional<EnemyDefinition> getEnemy(String id);

    /**
     * Look up a reference intake card by its ID.
     */
    Optional<ReferenceCard> getReferenceCard(String id);

    /**
     * Retrieve all loaded enemy definitions.
     */
    Collection<EnemyDefinition> getAllEnemies();

    /**
     * Retrieve all loaded reference cards.
     */
    Collection<ReferenceCard> getAllReferenceCards();
}

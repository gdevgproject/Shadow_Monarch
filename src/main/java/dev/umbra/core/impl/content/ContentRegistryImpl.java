package dev.umbra.core.impl.content;

import dev.umbra.core.contract.content.EnemyDefinition;
import dev.umbra.core.contract.content.ReferenceCard;
import dev.umbra.core.contract.content.UmbraContentRegistry;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class ContentRegistryImpl implements UmbraContentRegistry {
    private final Map<String, EnemyDefinition> enemies = new HashMap<>();
    private final Map<String, ReferenceCard> referenceCards = new HashMap<>();

    public void registerEnemy(EnemyDefinition enemy) {
        enemies.put(enemy.id(), enemy);
    }

    public void registerReferenceCard(ReferenceCard card) {
        referenceCards.put(card.referenceCardId(), card);
    }

    public void clear() {
        enemies.clear();
        referenceCards.clear();
    }

    @Override
    public Optional<EnemyDefinition> getEnemy(String id) {
        return Optional.ofNullable(enemies.get(id));
    }

    @Override
    public Optional<ReferenceCard> getReferenceCard(String id) {
        return Optional.ofNullable(referenceCards.get(id));
    }

    @Override
    public Collection<EnemyDefinition> getAllEnemies() {
        return Collections.unmodifiableCollection(enemies.values());
    }

    @Override
    public Collection<ReferenceCard> getAllReferenceCards() {
        return Collections.unmodifiableCollection(referenceCards.values());
    }
}

package dev.umbra.core.contract.progression;

import dev.umbra.core.contract.event.UmbraEvent;
import java.util.UUID;

/**
 * Event published when a player's shadow XP changes.
 */
public record UmbraPlayerXpChangedEvent(
    UUID playerUuid,
    int oldXp,
    int newXp,
    int currentLevel
) implements UmbraEvent {}

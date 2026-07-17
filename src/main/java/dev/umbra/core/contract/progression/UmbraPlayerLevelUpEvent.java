package dev.umbra.core.contract.progression;

import dev.umbra.core.contract.event.UmbraEvent;
import java.util.UUID;

/**
 * Event published when a player levels up.
 */
public record UmbraPlayerLevelUpEvent(
    UUID playerUuid,
    int oldLevel,
    int newLevel
) implements UmbraEvent {}

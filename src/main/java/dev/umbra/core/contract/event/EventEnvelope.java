package dev.umbra.core.contract.event;

import dev.umbra.core.contract.context.BoundedContext;
import java.util.UUID;

/**
 * An envelope wrapping an event with metadata.
 * Payload data must be immutable and gameplay context independent.
 */
public record EventEnvelope<T extends UmbraEvent>(
    UUID id,
    long timestamp,
    BoundedContext sender,
    T payload
) {
    public EventEnvelope(BoundedContext sender, T payload) {
        this(UUID.randomUUID(), System.currentTimeMillis(), sender, payload);
    }
}

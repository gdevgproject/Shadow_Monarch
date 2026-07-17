package dev.umbra.core.contract.event;

import java.util.function.Consumer;

/**
 * Interface representing the internal Event Bus of UMBRA.
 */
public interface UmbraEventBus {
    /**
     * Publishes an event wrapped in an envelope.
     */
    <T extends UmbraEvent> void publish(EventEnvelope<T> envelope);

    /**
     * Subscribes a consumer handler to receive events of a specific type.
     */
    <T extends UmbraEvent> void subscribe(Class<T> eventType, EventPriority priority, Consumer<EventEnvelope<T>> handler);

    /**
     * Subscribes a consumer handler to receive events with NORMAL priority.
     */
    default <T extends UmbraEvent> void subscribe(Class<T> eventType, Consumer<EventEnvelope<T>> handler) {
        subscribe(eventType, EventPriority.NORMAL, handler);
    }
}

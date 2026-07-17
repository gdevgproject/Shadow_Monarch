package dev.umbra.core.impl.event;

import dev.umbra.core.contract.event.Cancelable;
import dev.umbra.core.contract.event.EventEnvelope;
import dev.umbra.core.contract.event.EventPriority;
import dev.umbra.core.contract.event.UmbraEvent;
import dev.umbra.core.contract.event.UmbraEventBus;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Server-safe, thread-safe, priority-ordered implementation of UmbraEventBus.
 */
public final class EventBusImpl implements UmbraEventBus {
    private final Map<Class<? extends UmbraEvent>, Map<EventPriority, List<Consumer<EventEnvelope<?>>>>> subscribers = new ConcurrentHashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T extends UmbraEvent> void publish(EventEnvelope<T> envelope) {
        if (envelope == null || envelope.payload() == null) {
            return;
        }

        Class<? extends UmbraEvent> eventClass = envelope.payload().getClass();
        Map<EventPriority, List<Consumer<EventEnvelope<?>>>> priorityMap = subscribers.get(eventClass);
        if (priorityMap == null) {
            return;
        }

        T payload = envelope.payload();
        boolean isCancelable = payload instanceof Cancelable;

        for (EventPriority priority : EventPriority.values()) {
            List<Consumer<EventEnvelope<?>>> list = priorityMap.get(priority);
            if (list == null || list.isEmpty()) {
                continue;
            }

            for (Consumer<EventEnvelope<?>> handler : list) {
                if (isCancelable && ((Cancelable) payload).isCanceled() && priority != EventPriority.MONITOR) {
                    break;
                }
                try {
                    handler.accept((EventEnvelope<UmbraEvent>) envelope);
                } catch (Exception e) {
                    System.err.println("Error in event handler for " + eventClass.getSimpleName() + ": " + e.getMessage());
                }
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends UmbraEvent> void subscribe(Class<T> eventType, EventPriority priority, Consumer<EventEnvelope<T>> handler) {
        if (eventType == null || priority == null || handler == null) {
            return;
        }

        Map<EventPriority, List<Consumer<EventEnvelope<?>>>> priorityMap = subscribers.computeIfAbsent(eventType, k -> {
            Map<EventPriority, List<Consumer<EventEnvelope<?>>>> map = new EnumMap<>(EventPriority.class);
            for (EventPriority p : EventPriority.values()) {
                map.put(p, new CopyOnWriteArrayList<>());
            }
            return map;
        });

        Consumer<EventEnvelope<?>> typeErasedHandler = envelope -> handler.accept((EventEnvelope<T>) envelope);
        priorityMap.get(priority).add(typeErasedHandler);
    }
}

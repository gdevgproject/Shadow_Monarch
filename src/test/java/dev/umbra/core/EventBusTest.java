package dev.umbra.core;

import dev.umbra.core.contract.context.BoundedContext;
import dev.umbra.core.contract.event.Cancelable;
import dev.umbra.core.contract.event.EventEnvelope;
import dev.umbra.core.contract.event.EventPriority;
import dev.umbra.core.contract.event.UmbraEvent;
import dev.umbra.core.impl.event.EventBusImpl;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EventBusTest {

    static class TestEvent implements UmbraEvent {
        final String message;
        TestEvent(String message) { this.message = message; }
    }

    static class CancelableTestEvent implements UmbraEvent, Cancelable {
        private boolean canceled = false;
        @Override public boolean isCanceled() { return canceled; }
        @Override public void setCanceled(boolean canceled) { this.canceled = canceled; }
    }

    @Test
    public void testPriorityAndCancellation() {
        EventBusImpl eventBus = new EventBusImpl();
        List<String> order = new ArrayList<>();

        eventBus.subscribe(CancelableTestEvent.class, EventPriority.HIGH, env -> {
            order.add("HIGH");
            env.payload().setCanceled(true);
        });

        eventBus.subscribe(CancelableTestEvent.class, EventPriority.LOW, env -> {
            order.add("LOW");
        });

        eventBus.subscribe(CancelableTestEvent.class, EventPriority.HIGHEST, env -> {
            order.add("HIGHEST");
        });

        eventBus.subscribe(CancelableTestEvent.class, EventPriority.MONITOR, env -> {
            order.add("MONITOR");
        });

        CancelableTestEvent event = new CancelableTestEvent();
        eventBus.publish(new EventEnvelope<>(BoundedContext.PROGRESSION, event));

        // Execution order: LOW, HIGH (cancels event), HIGHEST (skipped), MONITOR (always runs)
        assertEquals(List.of("LOW", "HIGH", "MONITOR"), order);
        assertTrue(event.isCanceled());
    }
}

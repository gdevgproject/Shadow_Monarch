package dev.umbra;

import dev.umbra.core.contract.content.UmbraContentRegistry;
import dev.umbra.core.contract.event.UmbraEventBus;
import dev.umbra.core.contract.registry.UmbraServiceRegistry;
import dev.umbra.core.contract.scheduler.TickScheduler;
import dev.umbra.core.impl.content.ContentRegistryImpl;
import dev.umbra.core.impl.event.EventBusImpl;
import dev.umbra.core.impl.registry.ServiceRegistryImpl;
import dev.umbra.core.impl.scheduler.SchedulerImpl;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Common bootstrap entrypoint.
 */
public final class UmbraMod implements ModInitializer {
    public static final String MOD_ID = "umbra";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static final ServiceRegistryImpl SERVICE_REGISTRY = new ServiceRegistryImpl();
    private static final EventBusImpl EVENT_BUS = new EventBusImpl();
    private static final SchedulerImpl SCHEDULER = new SchedulerImpl();
    private static final ContentRegistryImpl CONTENT_REGISTRY = new ContentRegistryImpl();

    public static UmbraServiceRegistry getServiceRegistry() {
        return SERVICE_REGISTRY;
    }

    @Override
    public void onInitialize() {
        LOGGER.info("UMBRA bootstrap initialized");

        // Register core services
        SERVICE_REGISTRY.register(UmbraEventBus.class, EVENT_BUS);
        SERVICE_REGISTRY.register(TickScheduler.class, SCHEDULER);
        SERVICE_REGISTRY.register(UmbraContentRegistry.class, CONTENT_REGISTRY);

        // Register Server Tick lifecycle hook
        ServerTickEvents.START_SERVER_TICK.register(server -> SCHEDULER.tick());
    }
}

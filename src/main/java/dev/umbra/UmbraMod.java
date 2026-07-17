package dev.umbra;

import dev.umbra.core.contract.config.UmbraConfigService;
import dev.umbra.core.contract.content.UmbraContentRegistry;
import dev.umbra.core.contract.event.UmbraEventBus;
import dev.umbra.core.contract.registry.UmbraServiceRegistry;
import dev.umbra.core.contract.scheduler.TickScheduler;
import dev.umbra.core.contract.state.StateSaveService;
import dev.umbra.core.impl.config.UmbraConfigServiceImpl;
import dev.umbra.core.impl.content.ContentRegistryImpl;
import dev.umbra.core.impl.event.EventBusImpl;
import dev.umbra.core.impl.registry.ServiceRegistryImpl;
import dev.umbra.core.impl.scheduler.SchedulerImpl;
import dev.umbra.core.impl.state.StateSaveServiceImpl;
import java.nio.file.Path;
import java.util.UUID;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.world.level.storage.LevelResource;
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
    private static final StateSaveServiceImpl STATE_SAVE_SERVICE = new StateSaveServiceImpl();
    private static final UmbraConfigServiceImpl CONFIG_SERVICE = new UmbraConfigServiceImpl();

    public static UmbraServiceRegistry getServiceRegistry() {
        return SERVICE_REGISTRY;
    }

    @Override
    public void onInitialize() {
        LOGGER.info("UMBRA bootstrap initialized");

        // Load configuration
        CONFIG_SERVICE.load();

        // Register core services
        SERVICE_REGISTRY.register(UmbraEventBus.class, EVENT_BUS);
        SERVICE_REGISTRY.register(TickScheduler.class, SCHEDULER);
        SERVICE_REGISTRY.register(UmbraContentRegistry.class, CONTENT_REGISTRY);
        SERVICE_REGISTRY.register(StateSaveService.class, STATE_SAVE_SERVICE);
        SERVICE_REGISTRY.register(UmbraConfigService.class, CONFIG_SERVICE);

        // Register Server Tick lifecycle hook
        ServerTickEvents.START_SERVER_TICK.register(server -> SCHEDULER.tick());

        // Register Server Lifecycle save/stop hooks
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            Path worldDir = server.getWorldPath(LevelResource.ROOT);
            STATE_SAVE_SERVICE.onServerStart(worldDir);
        });

        ServerLifecycleEvents.BEFORE_SAVE.register((server, flush, force) -> {
            Path worldDir = server.getWorldPath(LevelResource.ROOT);
            STATE_SAVE_SERVICE.onWorldSave(worldDir);
        });

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            Path worldDir = server.getWorldPath(LevelResource.ROOT);
            STATE_SAVE_SERVICE.onServerStop(worldDir);
        });

        // Register Player Connection lifecycle hooks
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            UUID uuid = handler.player.getUUID();
            Path worldDir = server.getWorldPath(LevelResource.ROOT);
            STATE_SAVE_SERVICE.onPlayerJoin(uuid, worldDir);
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            UUID uuid = handler.player.getUUID();
            Path worldDir = server.getWorldPath(LevelResource.ROOT);
            STATE_SAVE_SERVICE.onPlayerLeave(uuid, worldDir);
        });
    }
}

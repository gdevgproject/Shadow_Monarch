package dev.umbra;

import dev.umbra.core.contract.config.UmbraConfigService;
import dev.umbra.core.contract.content.UmbraContentRegistry;
import dev.umbra.core.contract.event.UmbraEventBus;
import dev.umbra.core.contract.registry.UmbraServiceRegistry;
import dev.umbra.core.contract.scheduler.TickScheduler;
import dev.umbra.core.contract.progression.ProgressionService;
import dev.umbra.core.contract.state.StateSaveService;
import dev.umbra.core.contract.state.UmbraPlayerState;
import dev.umbra.core.impl.config.UmbraConfigServiceImpl;
import dev.umbra.core.impl.content.ContentRegistryImpl;
import dev.umbra.core.impl.event.EventBusImpl;
import dev.umbra.core.impl.progression.ProgressionServiceImpl;
import dev.umbra.core.impl.registry.ServiceRegistryImpl;
import dev.umbra.core.impl.scheduler.SchedulerImpl;
import dev.umbra.core.impl.state.StateSaveServiceImpl;
import java.nio.file.Path;
import java.util.UUID;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.world.level.storage.LevelResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dev.umbra.core.impl.command.UmbraCommand;
import dev.umbra.core.contract.combat.CombatService;
import dev.umbra.core.impl.combat.CombatServiceImpl;
import dev.umbra.core.impl.combat.CombatDummyEntity;
import dev.umbra.core.contract.quest.QuestService;
import dev.umbra.core.contract.quest.UmbraQuestClaimPayload;
import dev.umbra.core.impl.quest.QuestServiceImpl;

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
    private static final ProgressionServiceImpl PROGRESSION_SERVICE = new ProgressionServiceImpl();
    private static final CombatServiceImpl COMBAT_SERVICE = new CombatServiceImpl();
    private static final QuestServiceImpl QUEST_SERVICE = new QuestServiceImpl();

    // ---- Explore-distance accumulator (M1-08) ----------------------------------------
    // Runs on server thread only — no concurrency needed, plain HashMap is safe.
    // Maps UUID → last known [x, z] position.
    private static final java.util.Map<java.util.UUID, double[]> PLAYER_LAST_POS =
            new java.util.HashMap<>();
    // Maps UUID → fractional blocks accumulated since last integer-progress report.
    private static final java.util.Map<java.util.UUID, Double> PLAYER_DIST_ACCUM =
            new java.util.HashMap<>();
    // Throttle: process distance every DIST_TICK_INTERVAL ticks (8 ticks = 0.4 s).
    // At sprint speed (~5.6 blocks/s), ~2.2 blocks per interval — fine granularity.
    private static final int DIST_TICK_INTERVAL = 8;
    private static int distTickCounter = 0;
    // Per-check cap: >10 blocks in 8 ticks = teleport / impossible speed. Discard.
    private static final double DIST_MAX_PER_CHECK = 10.0;

    public static net.minecraft.world.entity.EntityType<CombatDummyEntity> COMBAT_DUMMY;

    public static UmbraServiceRegistry getServiceRegistry() {
        return SERVICE_REGISTRY;
    }

    @Override
    public void onInitialize() {
        LOGGER.info("UMBRA bootstrap initialized");

        // Load configuration
        CONFIG_SERVICE.load();

        // Register custom packet payloads S2C
        net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry.clientboundPlay().register(
            dev.umbra.core.contract.state.UmbraPlayerStatePayload.TYPE,
            dev.umbra.core.contract.state.UmbraPlayerStatePayload.CODEC
        );
        net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry.clientboundPlay().register(
            dev.umbra.core.contract.combat.UmbraCombatStatePayload.TYPE,
            dev.umbra.core.contract.combat.UmbraCombatStatePayload.CODEC
        );
        net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry.clientboundPlay().register(
            dev.umbra.core.contract.combat.UmbraDodgeStatePayload.TYPE,
            dev.umbra.core.contract.combat.UmbraDodgeStatePayload.CODEC
        );

        // Register custom packet payloads C2S
        net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry.serverboundPlay().register(
            dev.umbra.core.contract.state.UmbraStatsAllocatePayload.TYPE,
            dev.umbra.core.contract.state.UmbraStatsAllocatePayload.CODEC
        );
        net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry.serverboundPlay().register(
            dev.umbra.core.contract.state.UmbraStatsRespecPayload.TYPE,
            dev.umbra.core.contract.state.UmbraStatsRespecPayload.CODEC
        );
        net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry.serverboundPlay().register(
            dev.umbra.core.contract.combat.UmbraDodgeIntentPayload.TYPE,
            dev.umbra.core.contract.combat.UmbraDodgeIntentPayload.CODEC
        );
        net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry.serverboundPlay().register(
            UmbraQuestClaimPayload.TYPE,
            UmbraQuestClaimPayload.CODEC
        );

        // Register core services
        SERVICE_REGISTRY.register(UmbraEventBus.class, EVENT_BUS);
        SERVICE_REGISTRY.register(TickScheduler.class, SCHEDULER);
        SERVICE_REGISTRY.register(UmbraContentRegistry.class, CONTENT_REGISTRY);
        SERVICE_REGISTRY.register(StateSaveService.class, STATE_SAVE_SERVICE);
        SERVICE_REGISTRY.register(UmbraConfigService.class, CONFIG_SERVICE);
        SERVICE_REGISTRY.register(ProgressionService.class, PROGRESSION_SERVICE);
        SERVICE_REGISTRY.register(CombatService.class, COMBAT_SERVICE);
        SERVICE_REGISTRY.register(QuestService.class, QUEST_SERVICE);

        // Register entity type dynamically to prevent uninitialized registry crashes in unit tests
        COMBAT_DUMMY = net.minecraft.core.Registry.register(
            net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE,
            net.minecraft.resources.Identifier.fromNamespaceAndPath("umbra", "combat_dummy"),
            net.minecraft.world.entity.EntityType.Builder.of(CombatDummyEntity::new, net.minecraft.world.entity.MobCategory.MISC)
                .sized(0.6F, 1.8F)
                .build(net.minecraft.resources.ResourceKey.create(
                    net.minecraft.core.registries.Registries.ENTITY_TYPE,
                    net.minecraft.resources.Identifier.fromNamespaceAndPath("umbra", "combat_dummy")
                ))
        );

        // Register entity attributes
        net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry.register(
            COMBAT_DUMMY,
            CombatDummyEntity.createAttributes()
        );

        // Register Server Tick lifecycle hook
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            SCHEDULER.tick();
            COMBAT_SERVICE.tick(server);

            // EXPLORE_DISTANCE accumulator — throttled to every DIST_TICK_INTERVAL ticks.
            // For each online player: compute horizontal delta from last known position,
            // discard suspiciously large deltas (teleport/cheat), accumulate fractional
            // blocks, and fire onObjectiveProgress(EXPLORE_DISTANCE, intBlocks) when ≥1 block.
            if (++distTickCounter % DIST_TICK_INTERVAL == 0) {
                for (net.minecraft.server.level.ServerPlayer sp : server.getPlayerList().getPlayers()) {
                    java.util.UUID uuid = sp.getUUID();
                    double curX = sp.getX(), curZ = sp.getZ();
                    double[] last = PLAYER_LAST_POS.get(uuid);

                    if (last != null) {
                        double dx = curX - last[0];
                        double dz = curZ - last[1];
                        double dist = Math.sqrt(dx * dx + dz * dz);

                        if (dist <= DIST_MAX_PER_CHECK) { // discard teleport-scale movement
                            double accum = PLAYER_DIST_ACCUM.getOrDefault(uuid, 0.0) + dist;
                            int wholeBlocks = (int) accum;
                            if (wholeBlocks >= 1) {
                                QUEST_SERVICE.onObjectiveProgress(sp,
                                        dev.umbra.core.contract.quest.TrainingQuestDefinition.ObjectiveType.EXPLORE_DISTANCE,
                                        wholeBlocks);
                                accum -= wholeBlocks;
                            }
                            PLAYER_DIST_ACCUM.put(uuid, accum);
                        }
                    }

                    PLAYER_LAST_POS.put(uuid, new double[]{curX, curZ});
                }
            }
        });

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

            // Recalculate and update base HP/attributes on join
            PROGRESSION_SERVICE.updateDerivedAttributes(handler.player);

            STATE_SAVE_SERVICE.syncPlayerState(handler.player);

            // Initialise distance tracker to current position so the first check
            // does not produce a spurious large delta (M1-08).
            PLAYER_LAST_POS.put(uuid, new double[]{handler.player.getX(), handler.player.getZ()});
            PLAYER_DIST_ACCUM.put(uuid, 0.0);
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            UUID uuid = handler.player.getUUID();
            Path worldDir = server.getWorldPath(LevelResource.ROOT);
            STATE_SAVE_SERVICE.onPlayerLeave(uuid, worldDir);
            COMBAT_SERVICE.clearPlayerState(uuid);
            // Clean up distance accumulator to prevent memory leak (M1-08)
            PLAYER_LAST_POS.remove(uuid);
            PLAYER_DIST_ACCUM.remove(uuid);
        });

        net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            // A respawn creates a new ServerPlayer instance. Reset transient locks and immediately
            // sync persisted resources so Dodge cannot inherit a stale action from the old body.
            COMBAT_SERVICE.clearPlayerState(newPlayer.getUUID());
            PROGRESSION_SERVICE.updateDerivedAttributes(newPlayer);
            STATE_SAVE_SERVICE.syncPlayerState(newPlayer);
        });

        // Register mob-kill hook for quest objective tracking (M1-06/M1-07)
        net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents.AFTER_DEATH.register(
            (entity, damageSource) -> {
                net.minecraft.world.entity.Entity killer = damageSource.getEntity();
                if (!(killer instanceof net.minecraft.server.level.ServerPlayer serverPlayer)) return;
                // Only count naturally hostile creatures
                if (!(entity instanceof net.minecraft.world.entity.Mob mob)) return;
                if (mob.getType().getCategory() != net.minecraft.world.entity.MobCategory.MONSTER) return;
                QUEST_SERVICE.onObjectiveProgress(
                    serverPlayer,
                    dev.umbra.core.contract.quest.TrainingQuestDefinition.ObjectiveType.KILL_MOB
                );
            }
        );

        // Register block-break hook for MINE_BLOCK quest objective (M1-08)
        // Fires on both sides; guard with isClientSide() and ServerPlayer cast.
        // Counts any non-air block break in survival/adventure mode.
        // Creative mode intentionally included to allow quest testing via /gamemode.
        net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents.AFTER.register(
            (world, player, pos, blockState, blockEntity) -> {
                if (world.isClientSide()) return;
                if (!(player instanceof net.minecraft.server.level.ServerPlayer sp)) return;
                if (blockState.isAir()) return; // safety guard (should not happen post-break)
                QUEST_SERVICE.onObjectiveProgress(
                    sp,
                    dev.umbra.core.contract.quest.TrainingQuestDefinition.ObjectiveType.MINE_BLOCK
                );
            }
        );

        // Register commands
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            UmbraCommand.register(dispatcher);
        });

        // Register Serverbound network receivers
        net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.registerGlobalReceiver(
            dev.umbra.core.contract.state.UmbraStatsAllocatePayload.TYPE,
            (payload, context) -> {
                context.server().execute(() -> {
                    net.minecraft.server.level.ServerPlayer player = context.player();
                    if (player == null) return;

                    int strAdd = payload.strAdded();
                    int agiAdd = payload.agiAdded();
                    int vitAdd = payload.vitAdded();
                    int intAdd = payload.intAdded();
                    int perAdd = payload.perAdded();

                    if (strAdd < 0 || agiAdd < 0 || vitAdd < 0 || intAdd < 0 || perAdd < 0) {
                        return;
                    }

                    int sum = strAdd + agiAdd + vitAdd + intAdd + perAdd;
                    if (sum == 0) return;

                    StateSaveService stateSaveService = getServiceRegistry().locate(StateSaveService.class).orElseThrow();
                    UmbraPlayerState state = stateSaveService.getOrCreatePlayerState(player.getUUID());

                    if (sum <= state.getStatPoints()) {
                        state.setStrength(state.getStrength() + strAdd);
                        state.setAgility(state.getAgility() + agiAdd);
                        state.setVitality(state.getVitality() + vitAdd);
                        state.setIntelligence(state.getIntelligence() + intAdd);
                        state.setPerception(state.getPerception() + perAdd);
                        state.setStatPoints(state.getStatPoints() - sum);

                        ProgressionService progressionService = getServiceRegistry().locate(ProgressionService.class).orElseThrow();
                        progressionService.updateDerivedAttributes(player);

                        stateSaveService.syncPlayerState(player);

                        player.level().playSound(
                            null,
                            player.getX(), player.getY(), player.getZ(),
                            net.minecraft.sounds.SoundEvents.EXPERIENCE_ORB_PICKUP,
                            net.minecraft.sounds.SoundSource.PLAYERS,
                            0.5F, 1.5F
                        );
                    }
                });
            }
        );

        net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.registerGlobalReceiver(
            dev.umbra.core.contract.state.UmbraStatsRespecPayload.TYPE,
            (payload, context) -> {
                context.server().execute(() -> {
                    net.minecraft.server.level.ServerPlayer player = context.player();
                    if (player == null) return;

                    StateSaveService stateSaveService = getServiceRegistry().locate(StateSaveService.class).orElseThrow();
                    UmbraPlayerState state = stateSaveService.getOrCreatePlayerState(player.getUUID());

                    long gameTime = player.level().getGameTime();

                    if (!state.isJobChanged()) {
                        player.sendSystemMessage(net.minecraft.network.chat.Component.literal("§cYou must undergo a Job Change to respec attributes."));
                        return;
                    }
                    if (state.getEssence() < 10) {
                        player.sendSystemMessage(net.minecraft.network.chat.Component.literal("§cInsufficient Essence! Requires 10 Essence."));
                        return;
                    }
                    if (state.getLastRespecTime() != 0 && gameTime - state.getLastRespecTime() < 72000) {
                        long ticksLeft = 72000 - (gameTime - state.getLastRespecTime());
                        long secondsLeft = ticksLeft / 20;
                        player.sendSystemMessage(net.minecraft.network.chat.Component.literal("§cRespec on cooldown! Try again in " + (secondsLeft / 60) + " minutes."));
                        return;
                    }

                    int allocatedPoints = (state.getStrength() - 10) +
                                          (state.getAgility() - 10) +
                                          (state.getVitality() - 10) +
                                          (state.getIntelligence() - 10) +
                                          (state.getPerception() - 10);

                    state.setStrength(10);
                    state.setAgility(10);
                    state.setVitality(10);
                    state.setIntelligence(10);
                    state.setPerception(10);

                    state.setStatPoints(state.getStatPoints() + allocatedPoints);
                    state.setEssence(state.getEssence() - 10);
                    state.setLastRespecTime(gameTime);

                    ProgressionService progressionService = getServiceRegistry().locate(ProgressionService.class).orElseThrow();
                    progressionService.updateDerivedAttributes(player);

                    stateSaveService.syncPlayerState(player);

                    player.level().playSound(
                        null,
                        player.getX(), player.getY(), player.getZ(),
                        net.minecraft.sounds.SoundEvents.PLAYER_LEVELUP,
                        net.minecraft.sounds.SoundSource.PLAYERS,
                        0.8F, 0.5F
                    );
                    player.sendSystemMessage(net.minecraft.network.chat.Component.literal("§aAttributes successfully reset!"));
                });
            }
        );

        net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.registerGlobalReceiver(
            dev.umbra.core.contract.combat.UmbraDodgeIntentPayload.TYPE,
            (payload, context) -> context.server().execute(() -> {
                dev.umbra.core.contract.combat.DodgeDirection direction =
                    dev.umbra.core.contract.combat.DodgeDirection.fromWireId(payload.directionWireId());
                if (direction != null) {
                    COMBAT_SERVICE.requestDodge(context.player(), direction);
                }
            })
        );

        // C2S: quest claim (M1-06)
        net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.registerGlobalReceiver(
            UmbraQuestClaimPayload.TYPE,
            (payload, context) -> context.server().execute(() -> {
                net.minecraft.server.level.ServerPlayer player = context.player();
                if (player == null) return;
                QUEST_SERVICE.claimQuest(player, payload.questId());
            })
        );
    }
}

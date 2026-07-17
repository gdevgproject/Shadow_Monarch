package dev.umbra.core.impl.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import dev.umbra.UmbraMod;
import dev.umbra.core.contract.progression.ProgressionService;
import dev.umbra.core.contract.state.StateSaveService;
import dev.umbra.core.contract.state.UmbraPlayerState;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.server.permissions.PermissionLevel;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

/**
 * Brigadier-based command interface for managing player level and XP.
 */
public final class UmbraCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            literal("umbra")
                .requires(source -> source.checkPermission(
                    Identifier.fromNamespaceAndPath("umbra", "admin"),
                    PermissionLevel.byId(2)
                ))
                .then(literal("xp")
                    .then(literal("add")
                        .then(argument("player", EntityArgument.player())
                            .then(argument("amount", IntegerArgumentType.integer(0))
                                .executes(context -> {
                                    ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                    int amount = IntegerArgumentType.getInteger(context, "amount");

                                    ProgressionService progressionService = UmbraMod.getServiceRegistry()
                                        .locate(ProgressionService.class)
                                        .orElseThrow();

                                    progressionService.addXp(player, amount);

                                    StateSaveService stateSaveService = UmbraMod.getServiceRegistry()
                                        .locate(StateSaveService.class)
                                        .orElseThrow();
                                    UmbraPlayerState state = stateSaveService.getOrCreatePlayerState(player.getUUID());

                                    context.getSource().sendSuccess(
                                        () -> Component.literal("Added " + amount + " shadow XP to " + player.getGameProfile().name() +
                                            ". Current Level: " + state.getLevel() + ", XP: " + state.getShadowXp()),
                                        true
                                    );
                                    return 1;
                                })
                            )
                        )
                    )
                    .then(literal("set")
                        .then(argument("player", EntityArgument.player())
                            .then(argument("amount", IntegerArgumentType.integer(0))
                                .executes(context -> {
                                    ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                    int amount = IntegerArgumentType.getInteger(context, "amount");

                                    ProgressionService progressionService = UmbraMod.getServiceRegistry()
                                        .locate(ProgressionService.class)
                                        .orElseThrow();

                                    progressionService.setXp(player, amount);

                                    StateSaveService stateSaveService = UmbraMod.getServiceRegistry()
                                        .locate(StateSaveService.class)
                                        .orElseThrow();
                                    UmbraPlayerState state = stateSaveService.getOrCreatePlayerState(player.getUUID());

                                    context.getSource().sendSuccess(
                                        () -> Component.literal("Set shadow XP of " + player.getGameProfile().name() + " to " + amount +
                                            ". Current Level: " + state.getLevel() + ", XP: " + state.getShadowXp()),
                                        true
                                    );
                                    return 1;
                                })
                            )
                        )
                    )
                )
                .then(literal("level")
                    .then(literal("set")
                        .then(argument("player", EntityArgument.player())
                            .then(argument("level", IntegerArgumentType.integer(1, 10000))
                                .executes(context -> {
                                    ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                    int level = IntegerArgumentType.getInteger(context, "level");

                                    ProgressionService progressionService = UmbraMod.getServiceRegistry()
                                        .locate(ProgressionService.class)
                                        .orElseThrow();

                                    progressionService.setLevel(player, level);

                                    StateSaveService stateSaveService = UmbraMod.getServiceRegistry()
                                        .locate(StateSaveService.class)
                                        .orElseThrow();
                                    UmbraPlayerState state = stateSaveService.getOrCreatePlayerState(player.getUUID());

                                    context.getSource().sendSuccess(
                                        () -> Component.literal("Set level of " + player.getGameProfile().name() + " to " + level +
                                            ". Current Level: " + state.getLevel() + ", XP: " + state.getShadowXp()),
                                        true
                                    );
                                    return 1;
                                })
                            )
                        )
                    )
                )
                .then(literal("query")
                    .then(argument("player", EntityArgument.player())
                        .executes(context -> {
                            ServerPlayer player = EntityArgument.getPlayer(context, "player");

                            StateSaveService stateSaveService = UmbraMod.getServiceRegistry()
                                .locate(StateSaveService.class)
                                .orElseThrow();
                            UmbraPlayerState state = stateSaveService.getOrCreatePlayerState(player.getUUID());

                            context.getSource().sendSuccess(
                                () -> Component.literal("Player " + player.getGameProfile().name() +
                                    ": Level = " + state.getLevel() + ", XP = " + state.getShadowXp() +
                                    ", Rank = " + state.getRank() +
                                    ", Stats: STR=" + state.getStrength() +
                                    " AGI=" + state.getAgility() +
                                    " VIT=" + state.getVitality() +
                                    " INT=" + state.getIntelligence() +
                                    " PER=" + state.getPerception() +
                                    ", FreePoints = " + state.getStatPoints() +
                                    ", Essence = " + state.getEssence() +
                                    ", JobChanged = " + state.isJobChanged()),
                                false
                            );
                            return 1;
                        })
                    )
                )
                .then(literal("essence")
                    .then(literal("add")
                        .then(argument("player", EntityArgument.player())
                            .then(argument("amount", IntegerArgumentType.integer(0))
                                .executes(context -> {
                                    ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                    int amount = IntegerArgumentType.getInteger(context, "amount");

                                    StateSaveService stateSaveService = UmbraMod.getServiceRegistry()
                                        .locate(StateSaveService.class).orElseThrow();
                                    UmbraPlayerState state = stateSaveService.getOrCreatePlayerState(player.getUUID());
                                    state.setEssence(state.getEssence() + amount);
                                    stateSaveService.syncPlayerState(player);

                                    context.getSource().sendSuccess(
                                        () -> Component.literal("Added " + amount + " Essence to " + player.getGameProfile().name() + ". Current: " + state.getEssence()),
                                        true
                                    );
                                    return 1;
                                })
                            )
                        )
                    )
                    .then(literal("set")
                        .then(argument("player", EntityArgument.player())
                            .then(argument("amount", IntegerArgumentType.integer(0))
                                .executes(context -> {
                                    ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                    int amount = IntegerArgumentType.getInteger(context, "amount");

                                    StateSaveService stateSaveService = UmbraMod.getServiceRegistry()
                                        .locate(StateSaveService.class).orElseThrow();
                                    UmbraPlayerState state = stateSaveService.getOrCreatePlayerState(player.getUUID());
                                    state.setEssence(amount);
                                    stateSaveService.syncPlayerState(player);

                                    context.getSource().sendSuccess(
                                        () -> Component.literal("Set Essence of " + player.getGameProfile().name() + " to " + amount),
                                        true
                                    );
                                    return 1;
                                })
                            )
                        )
                    )
                )
                .then(literal("job")
                    .then(literal("set")
                        .then(argument("player", EntityArgument.player())
                            .then(argument("value", com.mojang.brigadier.arguments.BoolArgumentType.bool())
                                .executes(context -> {
                                    ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                    boolean val = com.mojang.brigadier.arguments.BoolArgumentType.getBool(context, "value");

                                    StateSaveService stateSaveService = UmbraMod.getServiceRegistry()
                                        .locate(StateSaveService.class).orElseThrow();
                                    UmbraPlayerState state = stateSaveService.getOrCreatePlayerState(player.getUUID());
                                    state.setJobChanged(val);
                                    stateSaveService.syncPlayerState(player);

                                    context.getSource().sendSuccess(
                                        () -> Component.literal("Set jobChanged of " + player.getGameProfile().name() + " to " + val),
                                        true
                                    );
                                    return 1;
                                })
                            )
                        )
                    )
                )
                .then(literal("respec")
                    .then(literal("bypass")
                        .then(argument("player", EntityArgument.player())
                            .executes(context -> {
                                ServerPlayer player = EntityArgument.getPlayer(context, "player");

                                StateSaveService stateSaveService = UmbraMod.getServiceRegistry()
                                    .locate(StateSaveService.class).orElseThrow();
                                UmbraPlayerState state = stateSaveService.getOrCreatePlayerState(player.getUUID());

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

                                ProgressionService progressionService = UmbraMod.getServiceRegistry()
                                    .locate(ProgressionService.class).orElseThrow();
                                progressionService.updateDerivedAttributes(player);

                                stateSaveService.syncPlayerState(player);

                                context.getSource().sendSuccess(
                                    () -> Component.literal("Bypassed respec cooldown/requirements and reset stats of " + player.getGameProfile().name()),
                                    true
                                );
                                return 1;
                            })
                        )
                    )
                )
                .then(literal("dummy")
                    .then(literal("spawn")
                        .executes(context -> {
                            net.minecraft.server.level.ServerLevel level = context.getSource().getLevel();
                            net.minecraft.world.phys.Vec3 pos = context.getSource().getPosition();
                            dev.umbra.core.impl.combat.CombatDummyEntity dummy = new dev.umbra.core.impl.combat.CombatDummyEntity(UmbraMod.COMBAT_DUMMY, level);
                            dummy.setPos(pos.x, pos.y, pos.z);
                            dummy.setYRot(context.getSource().getRotation().y);
                            dummy.setXRot(0.0F);
                            level.addFreshEntity(dummy);
                            context.getSource().sendSuccess(
                                () -> Component.literal("Spawned Combat Dummy at " + String.format("%.2f, %.2f, %.2f", pos.x, pos.y, pos.z)),
                                true
                            );
                            return 1;
                        })
                    )
                )
        );
    }
}

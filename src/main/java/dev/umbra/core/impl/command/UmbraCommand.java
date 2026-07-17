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
                                    ", Rank = " + state.getRank()),
                                false
                            );
                            return 1;
                        })
                    )
                )
        );
    }
}

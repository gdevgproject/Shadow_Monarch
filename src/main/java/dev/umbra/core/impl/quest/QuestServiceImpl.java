package dev.umbra.core.impl.quest;

import dev.umbra.UmbraMod;
import dev.umbra.core.contract.context.BoundedContext;
import dev.umbra.core.contract.event.EventEnvelope;
import dev.umbra.core.contract.event.UmbraEventBus;
import dev.umbra.core.contract.progression.ProgressionService;
import dev.umbra.core.contract.quest.ActiveQuestEntry;
import dev.umbra.core.contract.quest.QuestService;
import dev.umbra.core.contract.quest.TrainingQuestDefinition;
import dev.umbra.core.contract.quest.TrainingQuestDefinition.ObjectiveType;
import dev.umbra.core.contract.quest.UmbraQuestCompletedEvent;
import dev.umbra.core.contract.state.StateSaveService;
import dev.umbra.core.contract.state.UmbraPlayerState;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Server-authoritative implementation of {@link QuestService} for M1-06.
 *
 * <p>All built-in training quests are defined here as constants. Future versions
 * should load them from datapacks; for M1-06 the set is intentionally small so
 * one session proves the full open→progress→claim→reward cycle.
 *
 * <p>Invariants enforced:
 * <ul>
 *   <li>No streak / calendar lockout / daily-exclusive reward (R16, doc 22.7).</li>
 *   <li>Client sends only a quest-id intent; server validates and grants reward.</li>
 *   <li>A quest that has already been completed cannot be re-assigned or claimed.</li>
 * </ul>
 */
public final class QuestServiceImpl implements QuestService {

    // ---- Built-in quest definitions (M1-06 scope) --------------------------------

    /** Kill 5 hostile mobs — introductory training quest. */
    public static final String QUEST_FIRST_HUNT = "umbra:training/first_hunt";
    /** Kill 10 hostile mobs — follow-up training quest. */
    public static final String QUEST_HUNTER_INITIATE = "umbra:training/hunter_initiate";

    private static final Map<String, TrainingQuestDefinition> DEFINITIONS;

    static {
        Map<String, TrainingQuestDefinition> m = new LinkedHashMap<>();
        m.put(QUEST_FIRST_HUNT, new TrainingQuestDefinition(
                QUEST_FIRST_HUNT,
                "First Hunt",
                ObjectiveType.KILL_MOB,
                5,       // required kills
                150,     // XP reward  (doc 03.2.2: ~15% of level-up xp at level 1)
                2        // Essence reward
        ));
        m.put(QUEST_HUNTER_INITIATE, new TrainingQuestDefinition(
                QUEST_HUNTER_INITIATE,
                "Hunter Initiate",
                ObjectiveType.KILL_MOB,
                10,
                300,
                4
        ));
        DEFINITIONS = Collections.unmodifiableMap(m);
    }

    // ---- Helpers -----------------------------------------------------------------

    private StateSaveService stateSave() {
        return UmbraMod.getServiceRegistry()
                .locate(StateSaveService.class)
                .orElseThrow(() -> new IllegalStateException("StateSaveService not registered"));
    }

    private ProgressionService progression() {
        return UmbraMod.getServiceRegistry()
                .locate(ProgressionService.class)
                .orElseThrow(() -> new IllegalStateException("ProgressionService not registered"));
    }

    private UmbraEventBus eventBus() {
        return UmbraMod.getServiceRegistry()
                .locate(UmbraEventBus.class)
                .orElseThrow(() -> new IllegalStateException("UmbraEventBus not registered"));
    }

    // ---- QuestService implementation ---------------------------------------------

    @Override
    public Collection<TrainingQuestDefinition> getAllDefinitions() {
        return DEFINITIONS.values();
    }

    @Override
    public Optional<TrainingQuestDefinition> findDefinition(String questId) {
        return Optional.ofNullable(DEFINITIONS.get(questId));
    }

    @Override
    public boolean assignQuest(UUID playerUuid, String questId) {
        TrainingQuestDefinition def = DEFINITIONS.get(questId);
        if (def == null) return false;

        UmbraPlayerState state = stateSave().getOrCreatePlayerState(playerUuid);
        if (state.getActiveQuests().containsKey(questId)) return false;
        if (state.getCompletedQuestIds().contains(questId)) return false;

        state.getActiveQuests().put(questId, new ActiveQuestEntry(questId, 0));
        return true;
    }

    @Override
    public void onObjectiveProgress(ServerPlayer player, ObjectiveType type) {
        if (player == null) return;
        UUID uuid = player.getUUID();
        UmbraPlayerState state = stateSave().getOrCreatePlayerState(uuid);

        boolean anyUpdated = false;
        for (Map.Entry<String, ActiveQuestEntry> entry : state.getActiveQuests().entrySet()) {
            TrainingQuestDefinition def = DEFINITIONS.get(entry.getKey());
            if (def == null || def.getObjectiveType() != type) continue;

            ActiveQuestEntry progress = entry.getValue();
            int newProgress = progress.addProgress(1);
            anyUpdated = true;

            if (newProgress >= def.getRequiredCount()) {
                // Auto-notify player that quest is ready to claim
                player.sendSystemMessage(Component.literal(
                        "§a[UMBRA] Quest ready: §f" + def.getDisplayName()
                        + " §7(" + newProgress + "/" + def.getRequiredCount() + ")"
                        + " §a— use /umbra quest claim " + def.getId()));
            } else {
                player.sendSystemMessage(Component.literal(
                        "§e[UMBRA] §f" + def.getDisplayName()
                        + " §7progress: " + newProgress + "/" + def.getRequiredCount()));
            }
        }

        if (anyUpdated) {
            stateSave().syncPlayerState(player);
        }
    }

    @Override
    public boolean claimQuest(ServerPlayer player, String questId) {
        if (player == null) return false;
        UUID uuid = player.getUUID();

        TrainingQuestDefinition def = DEFINITIONS.get(questId);
        if (def == null) {
            player.sendSystemMessage(Component.literal("§c[UMBRA] Unknown quest: " + questId));
            return false;
        }

        UmbraPlayerState state = stateSave().getOrCreatePlayerState(uuid);

        if (state.getCompletedQuestIds().contains(questId)) {
            player.sendSystemMessage(Component.literal("§c[UMBRA] Quest already completed: " + def.getDisplayName()));
            return false;
        }

        ActiveQuestEntry entry = state.getActiveQuests().get(questId);
        if (entry == null) {
            player.sendSystemMessage(Component.literal("§c[UMBRA] Quest not active: " + def.getDisplayName()));
            return false;
        }

        if (entry.getCurrentProgress() < def.getRequiredCount()) {
            player.sendSystemMessage(Component.literal(
                    "§c[UMBRA] Quest not yet complete: " + def.getDisplayName()
                    + " (" + entry.getCurrentProgress() + "/" + def.getRequiredCount() + ")"));
            return false;
        }

        // --- Grant reward (server-authoritative) ---
        int xp = def.getXpReward();
        int essence = def.getEssenceReward();

        state.getActiveQuests().remove(questId);
        state.getCompletedQuestIds().add(questId);
        state.setEssence(state.getEssence() + essence);

        // XP grant via ProgressionService (handles level-up, events, sync)
        progression().addXp(player, xp);
        stateSave().syncPlayerState(player);

        player.sendSystemMessage(Component.literal(
                "§6[UMBRA] §fQuest complete: §a" + def.getDisplayName()
                + "§f! Reward: §a+" + xp + " XP§f, §d+" + essence + " Essence"));

        // Publish fact event
        eventBus().publish(new EventEnvelope<>(
                BoundedContext.PROGRESSION,
                new UmbraQuestCompletedEvent(uuid, questId, xp, essence)
        ));

        return true;
    }

    @Override
    public Collection<ActiveQuestEntry> getActiveQuests(UUID playerUuid) {
        return stateSave().getOrCreatePlayerState(playerUuid).getActiveQuests().values();
    }
}

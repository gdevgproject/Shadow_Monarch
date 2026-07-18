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

    // ---- Built-in quest definitions (M1-07 scope) --------------------------------
    // Balance rationale (doc 14.2 formula: EXP(L) = floor(60·L^1.85 + 25·L)):
    //   L1→L2 ≈ 85 XP · L2→L3 ≈ 266 XP · L3→L4 ≈ 532 XP · L4→L5 ≈ 879 XP
    //   Total L1→L20 ≈ 120,000 XP; quest target = 15% of total (doc 03.2.2).
    //   Three KILL_MOB quests contribute ~670 XP = ~0.56% of L1→L20 journey.
    //   Full 15% achieved when M1-08 MINE_BLOCK/EXPLORE quests are added.
    //
    // Essence balance: only current sink is Respec (10 Essence, unlocked post-JobChange).
    //   Three quests yield 6 Essence total = 60% of one future Respec. Player
    //   accumulates slowly, no dead-inventory feel (doc 22 principle 1).
    //
    // Monster Hunter "Hidden Teaching" principle:
    //   Quest count chosen so player MUST learn dodge or risk dying.
    //   3 kills = ~3 min Quick Win (Genshin principle); 25 kills = dodge mastery forced.

    /** Tier F-I: Kill 3 hostile mobs — Quick Win entry quest. */
    public static final String QUEST_FIRST_HUNT = "umbra:training/first_hunt";
    /** Tier F-II: Kill 10 hostile mobs — sustained combat, dodge awareness. */
    public static final String QUEST_HUNTER_INITIATE = "umbra:training/hunter_initiate";
    /** Tier E-I: Kill 25 hostile mobs — dodge mastery forced by attrition. */
    public static final String QUEST_IRON_WILL = "umbra:training/iron_will";

    private static final Map<String, TrainingQuestDefinition> DEFINITIONS;

    static {
        Map<String, TrainingQuestDefinition> m = new LinkedHashMap<>();

        // [Sơ Cấp I] Quick Win — 3 kills, ~47% of L1→L2 XP.
        // Teaching: basic combat. "Hệ Thống" voice: direct, numbered, no fluff.
        m.put(QUEST_FIRST_HUNT, new TrainingQuestDefinition(
                QUEST_FIRST_HUNT,
                "[I] Triệu Thử: Máu Đầu",
                ObjectiveType.KILL_MOB,
                3,     // 3 kills ≈ 3 min
                40,    // 40 XP = 47% of L1→L2 (85 XP) — meaningful Quick Win
                1      // 1 Essence — starter token
        ));

        // [Sơ Cấp II] Sustained combat — 10 kills, player should be L2-3.
        // Teaching: combat rhythm; a player who ignores dodge will take heavy damage.
        m.put(QUEST_HUNTER_INITIATE, new TrainingQuestDefinition(
                QUEST_HUNTER_INITIATE,
                "[II] Triệu Thử: Tôi Luyện",
                ObjectiveType.KILL_MOB,
                10,    // 10 kills ≈ 8-12 min
                180,   // 180 XP = 68% of L2→L3 (266 XP) — strong mid-session reward
                2      // 2 Essence — accumulating toward Respec
        ));

        // [Trung Cấp I] Endurance — 25 kills, player around L3-5, E rank.
        // Teaching: dodge is NECESSARY. 25 kills means HP will not survive without i-frames.
        // Deliberately high count to force skill acquisition (Monster Hunter principle).
        m.put(QUEST_IRON_WILL, new TrainingQuestDefinition(
                QUEST_IRON_WILL,
                "[III] Triệu Thử: Ý Chí Thép",
                ObjectiveType.KILL_MOB,
                25,    // 25 kills ≈ 20-30 min of active play
                450,   // 450 XP ≈ 51% of L4→L5 (879 XP) — significant long-session reward
                3      // 3 Essence — meaningful accumulation
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
                // Quest ready to claim — "Hệ Thống" completion voice
                player.sendSystemMessage(Component.literal(
                        "§a§l[Hệ Thống]§r §7Mục Tiêu Hoàn Thành: §f" + def.getDisplayName()
                        + " §8(" + newProgress + "/" + def.getRequiredCount() + ")"
                        + "\n§8  → §7Nhận thưởng: §e/umbra quest claim " + def.getId()));
            } else {
                player.sendSystemMessage(Component.literal(
                        "§8[Hệ Thống] §7" + def.getDisplayName()
                        + " §8— §a" + newProgress + "§7/§c" + def.getRequiredCount()));
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
            player.sendSystemMessage(Component.literal("§c§l[Hệ Thống]§r §cMã Nhiệm Vụ Không Hợp Lệ: §7" + questId));
            return false;
        }

        UmbraPlayerState state = stateSave().getOrCreatePlayerState(uuid);

        if (state.getCompletedQuestIds().contains(questId)) {
            player.sendSystemMessage(Component.literal("§8[Hệ Thống] §7" + def.getDisplayName() + " §8— §cĐã Hoàn Thành Trước Đó"));
            return false;
        }

        ActiveQuestEntry entry = state.getActiveQuests().get(questId);
        if (entry == null) {
            player.sendSystemMessage(Component.literal("§8[Hệ Thống] §7" + def.getDisplayName() + " §8— §cChưa Được Kích Hoạt"));
            return false;
        }

        if (entry.getCurrentProgress() < def.getRequiredCount()) {
            player.sendSystemMessage(Component.literal(
                    "§8[Hệ Thống] §7" + def.getDisplayName()
                    + " §8— §cTiến Độ Chưa Đủ §8(" + entry.getCurrentProgress() + "/" + def.getRequiredCount() + ")"));
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
                "§6§l[Hệ Thống]§r §fMục Tiêu Đạt: §a" + def.getDisplayName()
                + "§f!\n§8  Phần Thưởng: §a+" + xp + " Kinh Nghiệm§f, §d+" + essence + " Tinh Hoa"));

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

package dev.umbra.core.quest;

import dev.umbra.UmbraMod;
import dev.umbra.core.contract.event.UmbraEventBus;
import dev.umbra.core.contract.progression.ProgressionService;
import dev.umbra.core.contract.quest.ActiveQuestEntry;
import dev.umbra.core.contract.quest.QuestService;
import dev.umbra.core.contract.quest.TrainingQuestDefinition;
import dev.umbra.core.contract.state.StateSaveService;
import dev.umbra.core.contract.state.UmbraPlayerState;
import dev.umbra.core.contract.state.UmbraWorldState;
import dev.umbra.core.impl.event.EventBusImpl;
import dev.umbra.core.impl.quest.QuestServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * M1-07 — Quest constraint proof tests (R16, doc 22.7, doc 29).
 *
 * <p>Verifies:
 * <ol>
 *   <li>No streak: player can assign/complete quests after any gap in play time.</li>
 *   <li>No calendar lockout: {@link TrainingQuestDefinition} has no date/timestamp fields.</li>
 *   <li>No exclusive daily reward: no {@code isExclusiveDaily} or equivalent flag.</li>
 *   <li>Reward values match balance spec (doc 14.2 EXP curve rationale).</li>
 *   <li>Exactly three quests in M1-07 catalog (regression guard).</li>
 *   <li>Completed quest cannot be re-assigned.</li>
 * </ol>
 *
 * <p>Uses the same save/restore registry pattern as {@link QuestServiceTest}.
 */
class QuestConstraintTest {

    private QuestServiceImpl questService;
    private StubStateSaveService stateSave;

    // Saved originals for @AfterEach restore
    private UmbraEventBus savedEventBus;
    private StateSaveService savedStateSave;
    private ProgressionService savedProgression;

    @BeforeEach
    void setUp() {
        var registry = UmbraMod.getServiceRegistry();

        // Snapshot whatever is already registered (may be null in a clean test run)
        savedEventBus    = registry.locate(UmbraEventBus.class).orElse(null);
        savedStateSave   = registry.locate(StateSaveService.class).orElse(null);
        savedProgression = registry.locate(ProgressionService.class).orElse(null);

        stateSave = new StubStateSaveService();
        EventBusImpl eventBus = new EventBusImpl();

        registry.register(UmbraEventBus.class,     eventBus);
        registry.register(StateSaveService.class,  stateSave);
        registry.register(ProgressionService.class, new StubProgressionService());

        questService = new QuestServiceImpl();
    }

    @AfterEach
    void tearDown() {
        // Restore real implementations so other test classes are unaffected
        var registry = UmbraMod.getServiceRegistry();
        if (savedEventBus    != null) registry.register(UmbraEventBus.class,    savedEventBus);
        if (savedStateSave   != null) registry.register(StateSaveService.class,  savedStateSave);
        if (savedProgression != null) registry.register(ProgressionService.class, savedProgression);
    }

    // ---- Constraint 1: No Streak -------------------------------------------------

    /**
     * A player who "did nothing" for a long time must still be able to assign and
     * make progress on quests. No time-based expiry or streak penalty.
     * R16 (doc 22.7, doc 22 principle 7): "no streak lockout".
     */
    @Test
    void noStreak_playerCanAssignAndProgressAfterAnyGap() {
        UUID player = UUID.randomUUID();

        // Assign must succeed unconditionally
        boolean assigned = questService.assignQuest(player, QuestServiceImpl.QUEST_FIRST_HUNT);
        assertTrue(assigned, "Quest must be assignable at any time — no streak lockout");

        // Manually advance progress (simulates player returning after long break)
        UmbraPlayerState state = stateSave.getOrCreatePlayerState(player);
        ActiveQuestEntry entry = state.getActiveQuests().get(QuestServiceImpl.QUEST_FIRST_HUNT);
        assertNotNull(entry, "ActiveQuestEntry must exist after assign");

        TrainingQuestDefinition def = questService.findDefinition(QuestServiceImpl.QUEST_FIRST_HUNT).orElseThrow();
        for (int i = 0; i < def.getRequiredCount(); i++) {
            entry.addProgress(1);
        }

        assertEquals(def.getRequiredCount(), entry.getCurrentProgress(),
                "Progress must equal requiredCount — no time-based expiry");
    }

    // ---- Constraint 2: No Calendar Lockout ---------------------------------------

    /**
     * Neither {@link TrainingQuestDefinition} nor {@link ActiveQuestEntry} may
     * contain any date/calendar/timestamp/lockout field.
     * R16: "no calendar lockout".
     */
    @Test
    void noCalendarLockout_noDateFieldsInDefinitionOrEntry() {
        for (Field f : TrainingQuestDefinition.class.getDeclaredFields()) {
            String name = f.getName().toLowerCase();
            assertFalse(name.contains("date"),      "TrainingQuestDefinition must not have date field: "     + f.getName());
            assertFalse(name.contains("day"),       "TrainingQuestDefinition must not have day field: "      + f.getName());
            assertFalse(name.contains("calendar"),  "TrainingQuestDefinition must not have calendar field: " + f.getName());
            assertFalse(name.contains("lockout"),   "TrainingQuestDefinition must not have lockout field: "  + f.getName());
            assertFalse(name.contains("timestamp"), "TrainingQuestDefinition must not have timestamp field: "+ f.getName());
            assertFalse(name.contains("expire"),    "TrainingQuestDefinition must not have expire field: "   + f.getName());
        }
        for (Field f : ActiveQuestEntry.class.getDeclaredFields()) {
            String name = f.getName().toLowerCase();
            assertFalse(name.contains("date"),    "ActiveQuestEntry must not have date field: "    + f.getName());
            assertFalse(name.contains("expire"),  "ActiveQuestEntry must not have expire field: "  + f.getName());
            assertFalse(name.contains("lockout"), "ActiveQuestEntry must not have lockout field: " + f.getName());
        }
    }

    // ---- Constraint 3: No Exclusive Daily Reward ---------------------------------

    /**
     * {@link TrainingQuestDefinition} must have no flag that gates rewards behind
     * a real-time calendar (e.g. isExclusiveDaily, isDailyReward, etc.).
     * R16: "no daily-exclusive reward".
     */
    @Test
    void noExclusiveReward_noExclusiveFlagInDefinition() {
        for (Field f : TrainingQuestDefinition.class.getDeclaredFields()) {
            String name = f.getName().toLowerCase();
            assertFalse(name.contains("exclusive"), "Must not have exclusive field: " + f.getName());
            assertFalse(name.contains("daily"),     "Must not have daily field: "     + f.getName());
        }
    }

    // ---- Constraint 4: Reward Values (Balance Spec, doc 14.2) -------------------

    /**
     * Reward values for all three M1-07 quests must match the balance spec derived
     * from the doc 14.2 EXP curve: {@code EXP(L) = floor(60·L^1.85 + 25·L)}.
     *
     * <ul>
     *   <li>first_hunt:       3 kills · 40 XP (≈47% L1→L2=85)   · 1 Essence — Quick Win</li>
     *   <li>hunter_initiate: 10 kills · 180 XP (≈68% L2→L3=266) · 2 Essence — mid-session</li>
     *   <li>iron_will:       25 kills · 450 XP (≈51% L4→L5=879) · 3 Essence — endurance</li>
     * </ul>
     */
    @Test
    void rewardValues_matchBalanceSpec() {
        TrainingQuestDefinition q1 = questService.findDefinition(QuestServiceImpl.QUEST_FIRST_HUNT).orElseThrow();
        assertEquals(3,   q1.getRequiredCount(), "first_hunt: 3 kills (Quick Win, ~3 min)");
        assertEquals(40,  q1.getXpReward(),      "first_hunt: 40 XP = 47% of L1→L2 (85)");
        assertEquals(1,   q1.getEssenceReward(), "first_hunt: 1 Essence (starter token)");

        TrainingQuestDefinition q2 = questService.findDefinition(QuestServiceImpl.QUEST_HUNTER_INITIATE).orElseThrow();
        assertEquals(10,  q2.getRequiredCount(), "hunter_initiate: 10 kills (sustained combat)");
        assertEquals(180, q2.getXpReward(),      "hunter_initiate: 180 XP = 68% of L2→L3 (266)");
        assertEquals(2,   q2.getEssenceReward(), "hunter_initiate: 2 Essence");

        TrainingQuestDefinition q3 = questService.findDefinition(QuestServiceImpl.QUEST_IRON_WILL).orElseThrow();
        assertEquals(25,  q3.getRequiredCount(), "iron_will: 25 kills (dodge mastery forced)");
        assertEquals(450, q3.getXpReward(),      "iron_will: 450 XP = 51% of L4→L5 (879)");
        assertEquals(3,   q3.getEssenceReward(), "iron_will: 3 Essence");
    }

    // ---- Constraint 5: Catalog Contents (Regression Guard) ---------------------

    /**
     * The catalog must contain exactly 5 quests after M1-08:
     * first_hunt, hunter_initiate, iron_will (KILL_MOB) + miner_spirit (MINE_BLOCK)
     * + first_steps (EXPLORE_DISTANCE).
     * This guards against accidental catalog mutations between tickets.
     */
    @Test
    void catalog_containsAllExpectedQuests() {
        Collection<TrainingQuestDefinition> all = questService.getAllDefinitions();
        assertEquals(5, all.size(),
                "M1-08 catalog must have exactly 5 quests");

        // Kill quests (M1-07)
        assertTrue(questService.findDefinition(QuestServiceImpl.QUEST_FIRST_HUNT).isPresent(),
                "first_hunt must be in catalog");
        assertTrue(questService.findDefinition(QuestServiceImpl.QUEST_HUNTER_INITIATE).isPresent(),
                "hunter_initiate must be in catalog");
        assertTrue(questService.findDefinition(QuestServiceImpl.QUEST_IRON_WILL).isPresent(),
                "iron_will must be in catalog");

        // New objective types (M1-08)
        assertTrue(questService.findDefinition(QuestServiceImpl.QUEST_MINER_SPIRIT).isPresent(),
                "miner_spirit (MINE_BLOCK) must be in catalog");
        assertTrue(questService.findDefinition(QuestServiceImpl.QUEST_FIRST_STEPS).isPresent(),
                "first_steps (EXPLORE_DISTANCE) must be in catalog");

        // Verify objective types are correct
        assertEquals(TrainingQuestDefinition.ObjectiveType.MINE_BLOCK,
                questService.findDefinition(QuestServiceImpl.QUEST_MINER_SPIRIT)
                        .map(TrainingQuestDefinition::getObjectiveType).orElseThrow(),
                "miner_spirit must use MINE_BLOCK objective");
        assertEquals(TrainingQuestDefinition.ObjectiveType.EXPLORE_DISTANCE,
                questService.findDefinition(QuestServiceImpl.QUEST_FIRST_STEPS)
                        .map(TrainingQuestDefinition::getObjectiveType).orElseThrow(),
                "first_steps must use EXPLORE_DISTANCE objective");
    }

    // ---- Constraint 6: Completed Quest Cannot Be Re-assigned --------------------

    /**
     * Once a quest is completed it must never be re-assignable.
     * Prevents exploit farming and upholds one-reward-per-quest invariant.
     */
    @Test
    void completedQuest_cannotBeReassigned() {
        UUID player = UUID.randomUUID();
        questService.assignQuest(player, QuestServiceImpl.QUEST_FIRST_HUNT);

        // Mark completed directly (mirrors claimQuest server logic)
        UmbraPlayerState state = stateSave.getOrCreatePlayerState(player);
        state.getActiveQuests().remove(QuestServiceImpl.QUEST_FIRST_HUNT);
        state.getCompletedQuestIds().add(QuestServiceImpl.QUEST_FIRST_HUNT);

        boolean reassigned = questService.assignQuest(player, QuestServiceImpl.QUEST_FIRST_HUNT);
        assertFalse(reassigned, "Completed quest must not be re-assignable (no re-farming)");
    }

    // ---- Stubs ------------------------------------------------------------------

    static final class StubStateSaveService implements StateSaveService {
        private final Map<UUID, UmbraPlayerState> states = new HashMap<>();

        @Override public UmbraPlayerState getOrCreatePlayerState(UUID uuid) {
            return states.computeIfAbsent(uuid, u -> new UmbraPlayerState());
        }
        @Override public UmbraWorldState getWorldState()                         { return new UmbraWorldState(); }
        @Override public void loadPlayerState(UUID uuid, String json)            {}
        @Override public void loadWorldState(String json)                        {}
        @Override public String savePlayerState(UUID uuid)                       { return "{}"; }
        @Override public String saveWorldState()                                  { return "{}"; }
        @Override public void onPlayerJoin(UUID uuid, Path p)                   {}
        @Override public void onPlayerLeave(UUID uuid, Path p)                  {}
        @Override public void onWorldSave(Path p)                               {}
        @Override public void onServerStart(Path p)                             {}
        @Override public void onServerStop(Path p)                              {}
        @Override public void syncPlayerState(net.minecraft.server.level.ServerPlayer pl) {}
    }

    static final class StubProgressionService implements ProgressionService {
        @Override public void addXp(UUID uuid, int amount)                                           {}
        @Override public void addXp(net.minecraft.server.level.ServerPlayer p, int amount)          {}
        @Override public void setXp(UUID uuid, int amount)                                           {}
        @Override public void setXp(net.minecraft.server.level.ServerPlayer p, int amount)          {}
        @Override public void setLevel(UUID uuid, int level)                                         {}
        @Override public void setLevel(net.minecraft.server.level.ServerPlayer p, int level)        {}
        @Override public int  getRequiredXpForLevel(int level)                                       { return 85; }
        @Override public void updateDerivedAttributes(net.minecraft.server.level.ServerPlayer p)    {}
    }
}

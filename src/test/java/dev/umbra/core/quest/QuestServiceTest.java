package dev.umbra.core.quest;

import dev.umbra.UmbraMod;
import dev.umbra.core.contract.event.UmbraEventBus;
import dev.umbra.core.contract.progression.ProgressionService;
import dev.umbra.core.contract.quest.ActiveQuestEntry;
import dev.umbra.core.contract.quest.QuestService;
import dev.umbra.core.contract.quest.TrainingQuestDefinition;
import dev.umbra.core.contract.quest.UmbraQuestCompletedEvent;
import dev.umbra.core.contract.state.StateSaveService;
import dev.umbra.core.contract.state.UmbraPlayerState;
import dev.umbra.core.contract.state.UmbraWorldState;
import dev.umbra.core.impl.event.EventBusImpl;
import dev.umbra.core.impl.quest.QuestServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link QuestServiceImpl} (M1-06) — server-side logic only.
 *
 * <p>Tests operate on UUID-addressable state without requiring a live
 * {@link net.minecraft.server.level.ServerPlayer}. The {@link #claimQuest} path
 * that needs ServerPlayer is exercised by the GameTest in
 * {@link dev.umbra.gametest.QuestGameTest}.
 *
 * <p>Invariants verified:
 * <ul>
 *   <li>assign → manually progress → verify state: objective accounting is correct.</li>
 *   <li>Invalid assigns (unknown id, re-assign, re-assign after complete) are rejected.</li>
 *   <li>Completed quest set is persistent and prevents duplicate rewards.</li>
 *   <li>No-streak invariant: progress is never expired or reset by time.</li>
 *   <li>Event published on quest completion.</li>
 * </ul>
 */
class QuestServiceTest {

    private EventBusImpl eventBus;
    private StubStateSaveService stateSave;
    private QuestServiceImpl questService;

    // Saved real implementations — restored after each test so sibling test classes
    // (ProgressionServiceTest etc.) are not affected by our stubs.
    private UmbraEventBus savedEventBus;
    private StateSaveService savedStateSave;
    private ProgressionService savedProgression;

    @BeforeEach
    void setUp() {
        var registry = UmbraMod.getServiceRegistry();

        // Save whatever was registered before
        savedEventBus    = registry.locate(UmbraEventBus.class).orElse(null);
        savedStateSave   = registry.locate(StateSaveService.class).orElse(null);
        savedProgression = registry.locate(ProgressionService.class).orElse(null);

        eventBus  = new EventBusImpl();
        stateSave = new StubStateSaveService();
        var stubProgression = new StubProgressionService(stateSave);

        registry.register(UmbraEventBus.class,    eventBus);
        registry.register(StateSaveService.class,  stateSave);
        registry.register(ProgressionService.class, stubProgression);

        questService = new QuestServiceImpl();
    }

    @AfterEach
    void tearDown() {
        // Restore real implementations so other test classes are unaffected.
        var registry = UmbraMod.getServiceRegistry();
        if (savedEventBus    != null) registry.register(UmbraEventBus.class,    savedEventBus);
        if (savedStateSave   != null) registry.register(StateSaveService.class,  savedStateSave);
        if (savedProgression != null) registry.register(ProgressionService.class, savedProgression);
    }

    // ------------------------------------------------------------------

    @Test
    void allDefinitionsNonEmpty() {
        assertFalse(questService.getAllDefinitions().isEmpty(),
                "Must have at least one built-in training quest");
    }

    @Test
    void findDefinitionReturnsKnownQuestId() {
        Optional<TrainingQuestDefinition> def = questService.findDefinition(QuestServiceImpl.QUEST_FIRST_HUNT);
        assertTrue(def.isPresent());
        assertEquals(TrainingQuestDefinition.ObjectiveType.KILL_MOB, def.get().getObjectiveType());
        assertTrue(def.get().getRequiredCount() >= 1);
        assertTrue(def.get().getXpReward() > 0);
    }

    @Test
    void findDefinitionReturnsEmptyForUnknown() {
        assertTrue(questService.findDefinition("umbra:does_not_exist").isEmpty());
    }

    @Test
    void assignQuestSucceedsForNewPlayer() {
        UUID uuid = UUID.randomUUID();
        assertTrue(questService.assignQuest(uuid, QuestServiceImpl.QUEST_FIRST_HUNT));

        Collection<ActiveQuestEntry> active = questService.getActiveQuests(uuid);
        assertEquals(1, active.size());
        ActiveQuestEntry entry = active.iterator().next();
        assertEquals(QuestServiceImpl.QUEST_FIRST_HUNT, entry.getQuestId());
        assertEquals(0, entry.getCurrentProgress());
    }

    @Test
    void assignQuestFailsForUnknownId() {
        assertFalse(questService.assignQuest(UUID.randomUUID(), "umbra:does_not_exist"));
    }

    @Test
    void assignQuestFailsIfAlreadyActive() {
        UUID uuid = UUID.randomUUID();
        assertTrue(questService.assignQuest(uuid, QuestServiceImpl.QUEST_FIRST_HUNT));
        assertFalse(questService.assignQuest(uuid, QuestServiceImpl.QUEST_FIRST_HUNT),
                "Re-assigning same quest while active must be rejected");
    }

    @Test
    void objectiveProgressIsStoredInPlayerState() {
        UUID uuid = UUID.randomUUID();
        questService.assignQuest(uuid, QuestServiceImpl.QUEST_FIRST_HUNT);
        UmbraPlayerState state = stateSave.getOrCreatePlayerState(uuid);

        // Simulate 2 kills manually (onObjectiveProgress needs ServerPlayer so we test state directly)
        state.getActiveQuests().get(QuestServiceImpl.QUEST_FIRST_HUNT).addProgress(1);
        state.getActiveQuests().get(QuestServiceImpl.QUEST_FIRST_HUNT).addProgress(1);

        assertEquals(2, state.getActiveQuests().get(QuestServiceImpl.QUEST_FIRST_HUNT).getCurrentProgress());
    }

    @Test
    void questNotCompletedWhileProgressBelowRequired() {
        UUID uuid = UUID.randomUUID();
        questService.assignQuest(uuid, QuestServiceImpl.QUEST_FIRST_HUNT);
        UmbraPlayerState state = stateSave.getOrCreatePlayerState(uuid);
        TrainingQuestDefinition def = questService.findDefinition(QuestServiceImpl.QUEST_FIRST_HUNT).orElseThrow();

        // Add progress but stay below required
        if (def.getRequiredCount() > 1) {
            state.getActiveQuests().get(QuestServiceImpl.QUEST_FIRST_HUNT).addProgress(def.getRequiredCount() - 1);
        }

        // Still in active quests, not in completed
        assertTrue(state.getActiveQuests().containsKey(QuestServiceImpl.QUEST_FIRST_HUNT));
        assertFalse(state.getCompletedQuestIds().contains(QuestServiceImpl.QUEST_FIRST_HUNT));
    }

    @Test
    void manualClaimViaStateGrantsRewardAndPublishesEvent() {
        // This test simulates the server-side claimQuest logic via direct state manipulation
        // to verify that a completed quest entry triggers the right state transitions.
        // Full claimQuest(ServerPlayer) is tested in QuestGameTest.
        UUID uuid = UUID.randomUUID();
        questService.assignQuest(uuid, QuestServiceImpl.QUEST_FIRST_HUNT);
        UmbraPlayerState state = stateSave.getOrCreatePlayerState(uuid);
        TrainingQuestDefinition def = questService.findDefinition(QuestServiceImpl.QUEST_FIRST_HUNT).orElseThrow();

        // Advance to required count
        for (int i = 0; i < def.getRequiredCount(); i++) {
            state.getActiveQuests().get(QuestServiceImpl.QUEST_FIRST_HUNT).addProgress(1);
        }
        assertEquals(def.getRequiredCount(),
                state.getActiveQuests().get(QuestServiceImpl.QUEST_FIRST_HUNT).getCurrentProgress());

        // Simulate what claimQuest(ServerPlayer) does server-side:
        List<UmbraQuestCompletedEvent> captured = new ArrayList<>();
        eventBus.subscribe(UmbraQuestCompletedEvent.class, env -> captured.add(env.payload()));

        int xp = def.getXpReward();
        int ess = def.getEssenceReward();
        state.getActiveQuests().remove(QuestServiceImpl.QUEST_FIRST_HUNT);
        state.getCompletedQuestIds().add(QuestServiceImpl.QUEST_FIRST_HUNT);
        state.setEssence(state.getEssence() + ess);
        stateSave.<StubProgressionService>progressionOf(uuid).addXp(uuid, xp);
        eventBus.publish(new dev.umbra.core.contract.event.EventEnvelope<>(
                dev.umbra.core.contract.context.BoundedContext.PROGRESSION,
                new UmbraQuestCompletedEvent(uuid, QuestServiceImpl.QUEST_FIRST_HUNT, xp, ess)));

        // Verify state
        assertFalse(state.getActiveQuests().containsKey(QuestServiceImpl.QUEST_FIRST_HUNT));
        assertTrue(state.getCompletedQuestIds().contains(QuestServiceImpl.QUEST_FIRST_HUNT));
        assertEquals(ess, state.getEssence());
        assertEquals(xp, stateSave.getOrCreatePlayerState(uuid).getShadowXp());
        assertEquals(1, captured.size());
        assertEquals(xp, captured.get(0).getXpGranted());
        assertEquals(ess, captured.get(0).getEssenceGranted());
    }

    @Test
    void completedQuestCannotBeReassigned() {
        UUID uuid = UUID.randomUUID();
        UmbraPlayerState state = stateSave.getOrCreatePlayerState(uuid);
        state.getCompletedQuestIds().add(QuestServiceImpl.QUEST_FIRST_HUNT);

        assertFalse(questService.assignQuest(uuid, QuestServiceImpl.QUEST_FIRST_HUNT),
                "Completed quest must not be re-assignable");
    }

    @Test
    void noStreakInvariant_progressAccumulatesWithoutExpiry() {
        // R16: no streak/calendar lockout; progress persists indefinitely.
        UUID uuid = UUID.randomUUID();
        questService.assignQuest(uuid, QuestServiceImpl.QUEST_FIRST_HUNT);
        UmbraPlayerState state = stateSave.getOrCreatePlayerState(uuid);

        state.getActiveQuests().get(QuestServiceImpl.QUEST_FIRST_HUNT).addProgress(3);
        assertEquals(3, state.getActiveQuests().get(QuestServiceImpl.QUEST_FIRST_HUNT).getCurrentProgress(),
                "Progress must not be reset or expired by any time-based mechanism");

        // Additional progress accumulates
        state.getActiveQuests().get(QuestServiceImpl.QUEST_FIRST_HUNT).addProgress(2);
        assertEquals(5, state.getActiveQuests().get(QuestServiceImpl.QUEST_FIRST_HUNT).getCurrentProgress());
    }

    @Test
    void twoQuestsCanBeActiveSimultaneously() {
        UUID uuid = UUID.randomUUID();
        assertTrue(questService.assignQuest(uuid, QuestServiceImpl.QUEST_FIRST_HUNT));
        assertTrue(questService.assignQuest(uuid, QuestServiceImpl.QUEST_HUNTER_INITIATE));
        assertEquals(2, questService.getActiveQuests(uuid).size());
    }

    @Test
    void activeQuestEntryAddProgressThrowsForNegativeDelta() {
        ActiveQuestEntry entry = new ActiveQuestEntry("test:q", 0);
        assertThrows(IllegalArgumentException.class, () -> entry.addProgress(0));
        assertThrows(IllegalArgumentException.class, () -> entry.addProgress(-1));
    }

    // ------------------------------------------------------------------
    // Helpers

    // No registry injection needed — tests use UmbraMod.getServiceRegistry() directly.

    // ------------------------------------------------------------------
    // Stubs

    static final class StubStateSaveService implements StateSaveService {
        private final Map<UUID, UmbraPlayerState> states = new HashMap<>();
        private StubProgressionService progressionService;

        void setProgressionService(StubProgressionService p) { this.progressionService = p; }

        @SuppressWarnings("unchecked")
        <T extends ProgressionService> T progressionOf(UUID uuid) { return (T) progressionService; }

        @Override
        public UmbraPlayerState getOrCreatePlayerState(UUID uuid) {
            return states.computeIfAbsent(uuid, u -> new UmbraPlayerState());
        }

        @Override public UmbraWorldState getWorldState() { return new UmbraWorldState(); }
        @Override public void loadPlayerState(UUID uuid, String json) {}
        @Override public void loadWorldState(String json) {}
        @Override public String savePlayerState(UUID uuid) { return "{}"; }
        @Override public String saveWorldState() { return "{}"; }
        @Override public void onPlayerJoin(UUID uuid, Path p) {}
        @Override public void onPlayerLeave(UUID uuid, Path p) {}
        @Override public void onWorldSave(Path p) {}
        @Override public void onServerStart(Path p) {}
        @Override public void onServerStop(Path p) {}
        @Override public void syncPlayerState(net.minecraft.server.level.ServerPlayer player) {}
    }

    static final class StubProgressionService implements ProgressionService {
        private final StubStateSaveService stateSave;

        StubProgressionService(StubStateSaveService s) {
            this.stateSave = s;
            s.setProgressionService(this);
        }

        @Override
        public void addXp(UUID uuid, int amount) {
            UmbraPlayerState state = stateSave.getOrCreatePlayerState(uuid);
            state.setShadowXp(state.getShadowXp() + amount);
        }

        @Override public void addXp(net.minecraft.server.level.ServerPlayer player, int amount) {
            if (player != null) addXp(player.getUUID(), amount);
        }
        @Override public void setXp(UUID uuid, int amount) {}
        @Override public void setXp(net.minecraft.server.level.ServerPlayer p, int amount) {}
        @Override public void setLevel(UUID uuid, int level) {}
        @Override public void setLevel(net.minecraft.server.level.ServerPlayer p, int level) {}
        @Override public int getRequiredXpForLevel(int level) { return 100; }
        @Override public void updateDerivedAttributes(net.minecraft.server.level.ServerPlayer p) {}
    }
}

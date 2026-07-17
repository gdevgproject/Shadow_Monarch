package dev.umbra.core.progression;

import static org.junit.jupiter.api.Assertions.*;

import dev.umbra.UmbraMod;
import dev.umbra.core.contract.event.UmbraEventBus;
import dev.umbra.core.contract.progression.ProgressionService;
import dev.umbra.core.contract.progression.UmbraPlayerLevelUpEvent;
import dev.umbra.core.contract.progression.UmbraPlayerXpChangedEvent;
import dev.umbra.core.contract.state.StateSaveService;
import dev.umbra.core.contract.state.UmbraPlayerState;
import dev.umbra.core.impl.event.EventBusImpl;
import dev.umbra.core.impl.progression.ProgressionServiceImpl;
import dev.umbra.core.impl.state.StateSaveServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class ProgressionServiceTest {

    private static ProgressionService progressionService;
    private static StateSaveService stateSaveService;
    private static UmbraEventBus eventBus;
    private UUID testPlayerUuid;

    @BeforeAll
    public static void setUpRegistry() {
        var registry = UmbraMod.getServiceRegistry();

        if (registry.locate(UmbraEventBus.class).isEmpty()) {
            registry.register(UmbraEventBus.class, new EventBusImpl());
        }
        if (registry.locate(StateSaveService.class).isEmpty()) {
            registry.register(StateSaveService.class, new StateSaveServiceImpl());
        }
        if (registry.locate(ProgressionService.class).isEmpty()) {
            registry.register(ProgressionService.class, new ProgressionServiceImpl());
        }

        progressionService = registry.locate(ProgressionService.class).orElseThrow();
        stateSaveService = registry.locate(StateSaveService.class).orElseThrow();
        eventBus = registry.locate(UmbraEventBus.class).orElseThrow();
    }

    @BeforeEach
    public void setUp() {
        testPlayerUuid = UUID.randomUUID();
    }

    @Test
    public void testRequiredXpMathCurve() {
        // L = 1 -> 85
        assertEquals(85, progressionService.getRequiredXpForLevel(1));

        // L = 5 -> 60 * 5^1.85 + 25 * 5 = 1303
        assertEquals(1303, progressionService.getRequiredXpForLevel(5));

        // L = 20 -> 15812
        assertEquals(15812, progressionService.getRequiredXpForLevel(20));

        // L = 99 -> 297647
        assertEquals(297647, progressionService.getRequiredXpForLevel(99));

        // L = 100 (Prestige P=1) -> 50000 * (1 + 1*0.35) = 67500
        assertEquals(67500, progressionService.getRequiredXpForLevel(100));

        // L = 105 (Prestige P=6) -> 50000 * (1 + 6*0.35) = 155000
        assertEquals(155000, progressionService.getRequiredXpForLevel(105));

        // Invalid levels
        assertEquals(0, progressionService.getRequiredXpForLevel(0));
        assertEquals(0, progressionService.getRequiredXpForLevel(-5));
    }

    @Test
    public void testAddXpProgressAndLevelUp() {
        UmbraPlayerState state = stateSaveService.getOrCreatePlayerState(testPlayerUuid);
        state.setLevel(1);
        state.setShadowXp(0);

        // Required for level 1 -> 2 is 85
        progressionService.addXp(testPlayerUuid, 50);
        assertEquals(1, state.getLevel());
        assertEquals(50, state.getShadowXp());

        // Level up to 2: 50 + 40 = 90 >= 85 (remaining 5)
        progressionService.addXp(testPlayerUuid, 40);
        assertEquals(2, state.getLevel());
        assertEquals(5, state.getShadowXp());
    }

    @Test
    public void testAddXpMultipleLevelUps() {
        UmbraPlayerState state = stateSaveService.getOrCreatePlayerState(testPlayerUuid);
        state.setLevel(1);
        state.setShadowXp(0);

        // Required for L1: 85, L2: 266, L3: 532, etc.
        // Add large amount of XP: 1000
        progressionService.addXp(testPlayerUuid, 1000);

        // Let's trace L1 -> L2 (needs 85, remaining 915)
        // L2 -> L3: 266. Remaining 915 - 266 = 649.
        // L3 -> L4: 532. Remaining 649 - 532 = 117.
        // L4 -> L5: 879. Remaining 117 < 879.
        // End state should be Level 4 with 117 XP.
        assertEquals(4, state.getLevel());
        assertEquals(117, state.getShadowXp());
    }

    @Test
    public void testSetXpDirectly() {
        UmbraPlayerState state = stateSaveService.getOrCreatePlayerState(testPlayerUuid);
        state.setLevel(1);
        state.setShadowXp(0);

        progressionService.setXp(testPlayerUuid, 400);

        // 400 should trigger level ups:
        // L1 -> L2: 85 (remaining 315)
        // L2 -> L3: 266 (remaining 49)
        // L3 -> L4: 530 (remaining 49 < 530)
        // End state: L3, 49 XP
        assertEquals(3, state.getLevel());
        assertEquals(49, state.getShadowXp());
    }

    @Test
    public void testSetLevelDirectly() {
        UmbraPlayerState state = stateSaveService.getOrCreatePlayerState(testPlayerUuid);
        state.setLevel(1);
        state.setShadowXp(50);

        progressionService.setLevel(testPlayerUuid, 25);
        assertEquals(25, state.getLevel());
        assertEquals(0, state.getShadowXp()); // Should reset current XP progress to avoid anomalies
    }

    @Test
    public void testInvalidInputs() {
        assertThrows(IllegalArgumentException.class, () -> progressionService.addXp(testPlayerUuid, -10));
        assertThrows(IllegalArgumentException.class, () -> progressionService.setXp(testPlayerUuid, -5));
        assertThrows(IllegalArgumentException.class, () -> progressionService.setLevel(testPlayerUuid, 0));
        assertThrows(IllegalArgumentException.class, () -> progressionService.setLevel(testPlayerUuid, -1));
    }

    @Test
    public void testProgressionEventsPublished() {
        UmbraPlayerState state = stateSaveService.getOrCreatePlayerState(testPlayerUuid);
        state.setLevel(1);
        state.setShadowXp(0);

        List<UmbraPlayerXpChangedEvent> xpEvents = new ArrayList<>();
        List<UmbraPlayerLevelUpEvent> levelUpEvents = new ArrayList<>();

        eventBus.subscribe(UmbraPlayerXpChangedEvent.class, env -> xpEvents.add(env.payload()));
        eventBus.subscribe(UmbraPlayerLevelUpEvent.class, env -> levelUpEvents.add(env.payload()));

        progressionService.addXp(testPlayerUuid, 100);

        // Should publish one XP change event and one level up event (L1 -> L2)
        assertEquals(1, xpEvents.size());
        assertEquals(testPlayerUuid, xpEvents.get(0).playerUuid());
        assertEquals(0, xpEvents.get(0).oldXp());
        assertEquals(15, xpEvents.get(0).newXp());

        assertEquals(1, levelUpEvents.size());
        assertEquals(testPlayerUuid, levelUpEvents.get(0).playerUuid());
        assertEquals(1, levelUpEvents.get(0).oldLevel());
        assertEquals(2, levelUpEvents.get(0).newLevel());
    }
}

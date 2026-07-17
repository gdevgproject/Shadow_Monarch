package dev.umbra.core;

import dev.umbra.UmbraMod;
import dev.umbra.core.contract.config.DevConfig;
import dev.umbra.core.contract.config.PlayerConfig;
import dev.umbra.core.contract.config.ServerConfig;
import dev.umbra.core.contract.config.UmbraConfigService;
import dev.umbra.core.impl.scheduler.SchedulerImpl;
import org.junit.jupiter.api.Test;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

public class SchedulerBenchmarkTest {

    private static class MockConfigService implements UmbraConfigService {
        private final ServerConfig serverConfig = new ServerConfig();
        private final PlayerConfig playerConfig = new PlayerConfig();
        private final DevConfig devConfig = new DevConfig();

        @Override
        public PlayerConfig getPlayerConfig() { return playerConfig; }
        @Override
        public ServerConfig getServerConfig() { return serverConfig; }
        @Override
        public DevConfig getDevConfig() { return devConfig; }
        @Override
        public void load() {}
        @Override
        public void save() {}
        @Override
        public void reload() {}
    }

    private void spinMs(long ms) {
        long start = System.nanoTime();
        long ns = ms * 1_000_000L;
        while (System.nanoTime() - start < ns) {
            Thread.onSpinWait();
        }
    }

    @Test
    public void testTickBudgetCappingAndDeferral() {
        MockConfigService mockConfig = new MockConfigService();
        mockConfig.getServerConfig().setMaxTickBudgetMs(10); // 10ms budget
        UmbraMod.getServiceRegistry().register(UmbraConfigService.class, mockConfig);

        SchedulerImpl scheduler = new SchedulerImpl();
        AtomicInteger executedCount = new AtomicInteger(0);

        // Schedule 5 tasks, each taking 3ms. Total = 15ms.
        // With a 10ms budget:
        // Task 1: 3ms (total 3ms) - runs
        // Task 2: 3ms (total 6ms) - runs
        // Task 3: 3ms (total 9ms) - runs
        // Task 4: 3ms (total 12ms) - runs, exceeds budget, scheduler loop terminates.
        // Task 5: deferred to next tick.
        for (int i = 0; i < 5; i++) {
            scheduler.runOnTick(() -> {
                spinMs(3);
                executedCount.incrementAndGet();
            });
        }

        assertEquals(0, executedCount.get());
        assertEquals(5, scheduler.getPendingTasksCount());

        // First tick
        scheduler.tick();

        assertEquals(4, executedCount.get(), "Should execute exactly 4 tasks in the first tick (12ms elapsed >= 10ms budget)");
        assertEquals(1, scheduler.getPendingTasksCount(), "1 task should remain deferred");
        assertEquals(4, scheduler.getLastTickExecutedCount());
        assertTrue(scheduler.getLastTickDurationNs() > 0);

        // Second tick
        scheduler.tick();

        assertEquals(5, executedCount.get(), "Remaining task should run in the second tick");
        assertEquals(0, scheduler.getPendingTasksCount(), "No tasks should remain");
        assertEquals(1, scheduler.getLastTickExecutedCount());

        // Print benchmark report
        System.out.println("=== UMBRA SCHEDULER MSPT REPORT ===");
        System.out.printf("Average MSPT: %.3f ms%n", scheduler.getAverageTickDurationMs());
        System.out.printf("Last Tick Duration: %.3f ms%n", scheduler.getLastTickDurationNs() / 1_000_000.0);
        System.out.printf("Executed Tasks in Last Tick: %d%n", scheduler.getLastTickExecutedCount());
        System.out.printf("Pending Tasks: %d%n", scheduler.getPendingTasksCount());
        System.out.println("===================================");

        scheduler.shutdown();
    }
}

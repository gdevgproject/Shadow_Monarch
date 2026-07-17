package dev.umbra.core;

import dev.umbra.core.impl.scheduler.SchedulerImpl;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SchedulerTest {

    @Test
    public void testTickExecutionAndBudgetWarning() {
        SchedulerImpl scheduler = new SchedulerImpl();
        AtomicInteger runCount = new AtomicInteger(0);

        scheduler.runOnTick(runCount::incrementAndGet);
        scheduler.runWithBudget("test-budget", runCount::incrementAndGet, 10);

        assertEquals(0, runCount.get()); // Hasn't ticked yet

        scheduler.tick();
        assertEquals(2, runCount.get()); // Ticked

        scheduler.tick();
        assertEquals(2, runCount.get()); // Queue should be empty now

        scheduler.shutdown();
    }
}

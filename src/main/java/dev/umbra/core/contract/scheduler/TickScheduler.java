package dev.umbra.core.contract.scheduler;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * Interface representing the Central Scheduler of UMBRA.
 * It manages tasks scheduled for execution on the main game loop, keeping track of time budgets.
 */
public interface TickScheduler {
    /**
     * Schedules a task to run once on the main server thread at the next tick.
     */
    void runOnTick(Runnable task);

    /**
     * Schedules a task to run on the main server thread, warning if execution exceeds the budget (in milliseconds).
     */
    void runWithBudget(String taskName, Runnable task, long budgetMs);

    /**
     * Runs a heavy task asynchronously and executes a callback on the main tick thread when complete.
     */
    <T> void runAsync(Callable<T> asyncTask, Consumer<T> onMainThreadCallback);
}

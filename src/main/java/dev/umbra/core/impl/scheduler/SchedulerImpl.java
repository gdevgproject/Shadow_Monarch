package dev.umbra.core.impl.scheduler;

import dev.umbra.core.contract.scheduler.TickScheduler;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thread-safe execution engine implementing TickScheduler.
 */
public final class SchedulerImpl implements TickScheduler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerImpl.class);

    private final Queue<Runnable> tickQueue = new ConcurrentLinkedQueue<>();
    private final ExecutorService asyncExecutor = Executors.newWorkStealingPool();

    @Override
    public void runOnTick(Runnable task) {
        if (task != null) {
            tickQueue.add(task);
        }
    }

    @Override
    public void runWithBudget(String taskName, Runnable task, long budgetMs) {
        if (task == null) {
            return;
        }
        runOnTick(() -> {
            long startTime = System.nanoTime();
            try {
                task.run();
            } finally {
                long durationMs = (System.nanoTime() - startTime) / 1_000_000;
                if (durationMs > budgetMs) {
                    LOGGER.warn("Task '{}' exceeded execution budget! Took {}ms (budget: {}ms)", taskName, durationMs, budgetMs);
                }
            }
        });
    }

    @Override
    public <T> void runAsync(Callable<T> asyncTask, Consumer<T> onMainThreadCallback) {
        if (asyncTask == null) {
            return;
        }
        asyncExecutor.submit(() -> {
            try {
                T result = asyncTask.call();
                if (onMainThreadCallback != null) {
                    runOnTick(() -> onMainThreadCallback.accept(result));
                }
            } catch (Exception e) {
                LOGGER.error("Error executing asynchronous task", e);
            }
        });
    }

    /**
     * Executes all pending tick tasks. Called at the start of every server tick.
     */
    public void tick() {
        Runnable task;
        while ((task = tickQueue.poll()) != null) {
            try {
                task.run();
            } catch (Exception e) {
                LOGGER.error("Error executing tick task", e);
            }
        }
    }

    public void shutdown() {
        asyncExecutor.shutdown();
    }
}

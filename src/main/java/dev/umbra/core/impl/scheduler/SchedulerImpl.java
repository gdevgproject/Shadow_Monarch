package dev.umbra.core.impl.scheduler;

import dev.umbra.UmbraMod;
import dev.umbra.core.contract.config.UmbraConfigService;
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

    private long lastTickDurationNs = 0;
    private int lastTickExecutedCount = 0;
    private double averageTickDurationMs = 0.0;
    private long lastLogWarningTimeMs = 0;

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

    @Override
    public long getLastTickDurationNs() {
        return lastTickDurationNs;
    }

    @Override
    public int getLastTickExecutedCount() {
        return lastTickExecutedCount;
    }

    @Override
    public int getPendingTasksCount() {
        return tickQueue.size();
    }

    @Override
    public double getAverageTickDurationMs() {
        return averageTickDurationMs;
    }

    private long getMaxTickBudgetNs() {
        try {
            var registry = UmbraMod.getServiceRegistry();
            if (registry != null) {
                var configService = registry.locate(UmbraConfigService.class).orElse(null);
                if (configService != null && configService.getServerConfig() != null) {
                    return configService.getServerConfig().getMaxTickBudgetMs() * 1_000_000L;
                }
            }
        } catch (Throwable ignored) {
        }
        return 15 * 1_000_000L; // default 15ms
    }

    /**
     * Executes all pending tick tasks. Called at the start of every server tick.
     */
    public void tick() {
        long startTime = System.nanoTime();
        long budgetNs = getMaxTickBudgetNs();
        int executedCount = 0;
        Runnable task;

        while ((task = tickQueue.poll()) != null) {
            try {
                task.run();
                executedCount++;
            } catch (Exception e) {
                LOGGER.error("Error executing tick task", e);
            }

            long elapsedNs = System.nanoTime() - startTime;
            if (elapsedNs >= budgetNs) {
                long currentTimeMs = System.currentTimeMillis();
                if (currentTimeMs - lastLogWarningTimeMs > 5000) {
                    LOGGER.warn("Central Scheduler: tick budget exceeded! Took {}ms (budget: {}ms). Deferring remaining {} tasks.",
                            elapsedNs / 1_000_000, budgetNs / 1_000_000, tickQueue.size());
                    lastLogWarningTimeMs = currentTimeMs;
                }
                break;
            }
        }

        long durationNs = System.nanoTime() - startTime;
        this.lastTickDurationNs = durationNs;
        this.lastTickExecutedCount = executedCount;

        double durationMs = durationNs / 1_000_000.0;
        if (this.averageTickDurationMs == 0.0) {
            this.averageTickDurationMs = durationMs;
        } else {
            this.averageTickDurationMs = 0.05 * durationMs + 0.95 * this.averageTickDurationMs;
        }
    }

    public void shutdown() {
        asyncExecutor.shutdown();
    }
}

package dev.umbra.core.contract.event;

/**
 * Priorities for event subscribers.
 * Subscriptions are executed from LOWEST to HIGHEST, and MONITOR is run last for logging/auditing.
 */
public enum EventPriority {
    LOWEST,
    LOW,
    NORMAL,
    HIGH,
    HIGHEST,
    MONITOR
}

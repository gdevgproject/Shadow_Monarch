package dev.umbra.core.contract.event;

/**
 * Interface for events that can be canceled by their handlers.
 */
public interface Cancelable {
    boolean isCanceled();
    void setCanceled(boolean canceled);
}

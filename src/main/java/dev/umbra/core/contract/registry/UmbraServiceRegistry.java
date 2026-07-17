package dev.umbra.core.contract.registry;

import java.util.Optional;

/**
 * Service locator for retrieving cross-context service interfaces.
 * Prevents direct coupling between context implementations.
 */
public interface UmbraServiceRegistry {
    /**
     * Registers a service implementation for a given interface class.
     */
    <T> void register(Class<T> serviceType, T implementation);

    /**
     * Locates a registered service.
     */
    <T> Optional<T> locate(Class<T> serviceType);
}

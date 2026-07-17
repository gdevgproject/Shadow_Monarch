package dev.umbra.core.impl.registry;

import dev.umbra.core.contract.registry.UmbraServiceRegistry;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe implementation of UmbraServiceRegistry.
 */
public final class ServiceRegistryImpl implements UmbraServiceRegistry {
    private final Map<Class<?>, Object> services = new ConcurrentHashMap<>();

    @Override
    public <T> void register(Class<T> serviceType, T implementation) {
        if (serviceType != null && implementation != null) {
            services.put(serviceType, implementation);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> locate(Class<T> serviceType) {
        if (serviceType == null) {
            return Optional.empty();
        }
        Object implementation = services.get(serviceType);
        return Optional.ofNullable((T) implementation);
    }
}

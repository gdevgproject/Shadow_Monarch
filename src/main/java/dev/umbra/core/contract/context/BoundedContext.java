package dev.umbra.core.contract.context;

/**
 * Defines the bounded contexts of UMBRA.
 */
public enum BoundedContext {
    CORE("core"),
    PROGRESSION("progression"),
    COMBAT("combat"),
    SHADOWS("shadows"),
    AI("ai"),
    DUNGEONS("dungeons"),
    WORLD("world"),
    STRATA("strata"),
    ITEMS("items"),
    ECONOMY("economy"),
    CLIENT("client");

    private final String id;

    BoundedContext(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}

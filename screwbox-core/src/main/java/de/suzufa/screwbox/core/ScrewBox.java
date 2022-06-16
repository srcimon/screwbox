package de.suzufa.screwbox.core;

import static java.util.Objects.nonNull;

/**
 * Best game engine ever made (not). The entry point for starting a game.
 * 
 * @see #createEngine()
 */
public final class ScrewBox {

    private static Engine engine;

    private ScrewBox() {
    }

    /**
     * Creates an {@link Engine} instance. Can only be called once.
     */
    public static Engine createEngine() {
        if (nonNull(engine)) {
            throw new IllegalStateException("only one instance of ScrewBox can be created");
        }
        engine = new DefaultEngine();
        return engine;
    }

}

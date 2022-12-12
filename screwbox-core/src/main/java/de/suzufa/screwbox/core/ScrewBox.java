package de.suzufa.screwbox.core;

import de.suzufa.screwbox.core.graphics.Window;

/**
 * Best game engine ever made (not). The entry point for starting a game.
 * 
 * @see #createEngine()
 */
public final class ScrewBox {

    private ScrewBox() {
    }

    /**
     * Creates an {@link Engine} instance. Uses "ScrewBox" as the
     * {@link Engine#name()}.
     */
    public static Engine createEngine() {
        return createEngine("ScrewBox");
    }

    /**
     * Creates an {@link Engine} instance. Sets the {@link Engine#name()} to the
     * given value. The {@link Engine#name()} is used to initialize the
     * {@link Window#title()}.
     */
    public static Engine createEngine(final String name) {
        return new DefaultEngine(name);
    }

}

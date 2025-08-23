package dev.screwbox.core;

import dev.screwbox.core.window.Window;

import static java.util.Objects.requireNonNull;

/**
 * Best game engine ever made (not). The entry point for starting a game via {@link ScrewBox#createEngine()}.
 *
 * @see <a href="https://screwbox.dev">Documentation</a>
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
        requireNonNull(name, "name must not be null");
        return new DefaultEngine(name);
    }

}

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
     * specified value. The {@link Engine#name()} is used to initialize the
     * {@link Window#title()}. Auto detects recommended {@link RenderingMode}.
     */
    public static Engine createEngine(final String name) {
        return createEngine(name, RenderingMode.autodetect());
    }

    /**
     * Creates an {@link Engine} instance. Sets the {@link Engine#name()} to the
     * specified value and tries to use the specified {@link RenderingMode}.
     * The {@link Engine#name()} is used to initialize the s{@link Window#title()}.
     */
    public static Engine createEngine(final String name, final RenderingMode renderingMode) {
        requireNonNull(name, "name must not be null");
        renderingMode.applyMode();
        return new DefaultEngine(name);
    }

}

package de.suzufa.screwbox.core;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import de.suzufa.screwbox.core.graphics.Window;
import de.suzufa.screwbox.core.graphics.internal.DefaultFrameAdapter;
import de.suzufa.screwbox.core.graphics.internal.FakeFrameAdapter;

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
     * Creates an {@link Engine} instance. Can only be called once. Uses "ScrewBox"
     * as the {@link Engine#name()}.
     */
    public static Engine createEngine() {
        return createEngine("ScrewBox");
    }

    /**
     * Creates an {@link Engine} instance. Can only be called once. Sets the
     * {@link Engine#name()} to the given value. The {@link Engine#name()} is used
     * to initialize the {@link Window#title()}.
     */
    public static Engine createEngine(final String name) {
        requireNonNull(name, "name must not be null");

        if (nonNull(engine)) {
            throw new IllegalStateException("only one instance of ScrewBox can be created");
        }
        engine = new DefaultEngine(name, new DefaultFrameAdapter());
        return engine;
    }

    /**
     * Creates an {@link Engine} instance for testing purposes only. It is possible
     * to create more than one instance.
     */
    public static Engine createHeadlessEngine() {
        return new DefaultEngine("Headless", new FakeFrameAdapter());
    }

}

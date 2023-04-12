package io.github.srcimon.screwbox.core;

import io.github.srcimon.screwbox.core.window.Window;

import java.lang.management.ManagementFactory;
import java.util.List;

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
        validateJvmOptions();
        return new DefaultEngine(name);
    }

    private static void validateJvmOptions() {
        final List<String> jvmOptions = ManagementFactory.getRuntimeMXBean().getInputArguments();
        if (!jvmOptions.contains("-Dsun.java2d.opengl=true")) {
            throw new IllegalStateException("Please run application with JVM Option '-Dsun.java2d.opengl=true' to avoid massive fps drop.");
        }
    }

}

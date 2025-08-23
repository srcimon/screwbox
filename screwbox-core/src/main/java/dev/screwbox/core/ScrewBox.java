package dev.screwbox.core;

import dev.screwbox.core.window.Window;

import javax.swing.*;

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
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
        System.setProperty("sun.java2d.opengl", "true");
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

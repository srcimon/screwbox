package de.suzufa.screwbox.core.loop;

/**
 * Provides access to current {@link Metrics} and to control the target frames
 * per second.
 */
public interface GameLoop {

    /**
     * The default target frames per second.
     */
    public static final int DEFAULT_TARGET_FPS = 120;

    /**
     * Returns current game loop metrics like frames per second and update times.
     */
    Metrics metrics();

    /**
     * Sets the games target frames per second. Default value is
     * {@link #DEFAULT_TARGET_FPS}. Setting target fps below that value is not
     * allowed.
     */
    GameLoop setTargetFps(int targetFps);

    /**
     * Returns the current target frames per second.
     */
    int targetFps();
}

package dev.screwbox.core.loop;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Time;
import dev.screwbox.core.environment.EntitySystem;

/**
 * Provides access to current performance metrics and to controls the target frames per second.
 *
 * @see <a href="http://screwbox.dev/docs/core-modules/loop">Documentation</a>
 */
public interface Loop {

    /**
     * The minimum target frames per second.
     */
    int MIN_TARGET_FPS = 120;

    /**
     * Returns the current game speed. Default 1.0
     *
     * @since 2.10.0
     */
    double speed();

    /**
     * Sets the game speed. This basically changes the {@link #delta()} value received by all
     * {@link EntitySystem systems}. Default is 1.0. Valid range is 0 to 10.
     *
     * @since 2.10.0
     */
    void setSpeed(double speed);

    /**
     * Sets the games target frames per second. Default value is
     * {@link #MIN_TARGET_FPS}. Setting target fps below that value is not allowed.
     */
    Loop setTargetFps(int targetFps);

    /**
     * Sets the games target frames per second to {@link Integer#MAX_VALUE} which is
     * as good as unlimited.
     */
    Loop unlockFps();

    /**
     * Returns the current target frames per second.
     */
    int targetFps();

    /**
     * Returns the current count of frames per second.
     */
    int fps();

    /**
     * Returns the {@link Duration} that the last update took.
     */
    Duration updateDuration();

    /**
     * Returns the running time of the game engine. This is the {@link Duration}
     * since last time calling {@link Engine#start()}.
     *
     * @see #startTime()
     */
    Duration runningTime();

    /**
     * Returns the time the {@link Loop} was started for the last time. This is
     * the {@link Duration} since last time calling {@link Engine#start()}.
     *
     * @see #runningTime()
     */
    Time startTime();

    /**
     * Returns an abstraction of delta time between two frames. It's highly
     * recommended to use this value to multiply with any kind of steady movement in
     * the game to avoid changes in the game speed in dependency to the frame rate
     * ({@link #fps()}).
     *
     * @see #delta(double)
     */
    double delta();

    /**
     * Same as {@link #delta()} but multiplies the result by the given factor.
     */
    default double delta(final double factor) {
        return delta() * factor;
    }

    /**
     * Returns the {@link Time} of the last update cycle. Prefer using this instead of {@link Time#now()} for
     * better performance.
     */
    Time time();

    /**
     * Returns the number of the current frame (Each iteration of the game loop is called frame).
     */
    long frameNumber();
}

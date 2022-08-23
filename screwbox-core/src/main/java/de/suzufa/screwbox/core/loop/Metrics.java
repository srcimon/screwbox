package de.suzufa.screwbox.core.loop;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.ScrewBox;
import de.suzufa.screwbox.core.Time;

public interface Metrics {

    int framesPerSecond();

    Duration durationOfLastUpdate();

    /**
     * Returns the running time of the game engine. Time since calling
     * {@link ScrewBox#createEngine()}.
     */
    Duration runningTime();

    /**
     * Returns an abstraction of delta time between two frames. It's highly
     * recommended to use this value to multiply with any kind of steady movement in
     * the game to avoid changes in the game speed in dependency to the frame rate
     * ({@link #framesPerSecond()}).
     */
    double delta();

    /**
     * Returns the {@link Time} of the last update cycle.
     */
    Time lastUpdate();

    /**
     * Returns the number of the current frame (Each iteration of the game loop is
     * known as frame).
     */
    long frameNumber();
}

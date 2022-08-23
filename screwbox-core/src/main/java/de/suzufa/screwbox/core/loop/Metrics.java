package de.suzufa.screwbox.core.loop;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.Time;

public interface Metrics {

    int framesPerSecond();

    Duration durationOfLastUpdate();

    Duration durationOfRuntime();

    double updateFactor();

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

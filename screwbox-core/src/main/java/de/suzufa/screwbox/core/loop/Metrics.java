package de.suzufa.screwbox.core.loop;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.Time;

public interface Metrics {

    int framesPerSecond();

    Duration durationOfLastUpdate();

    Duration durationSinceLastUpdate();

    Duration durationOfRuntime();

    double updateFactor();

    Time timeOfLastUpdate();

    long frameNumber();
}

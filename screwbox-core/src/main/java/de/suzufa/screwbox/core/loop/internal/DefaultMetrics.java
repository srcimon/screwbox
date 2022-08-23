package de.suzufa.screwbox.core.loop.internal;

import static de.suzufa.screwbox.core.Time.NANOS_PER_SECOND;
import static java.lang.Math.min;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.loop.Metrics;

public class DefaultMetrics implements Metrics {

    private static final int CRITICAL_FPS_COUNT = 10;
    private int fps = 0;
    private long frameNumber;
    private double delta = 0;
    private Duration updateDuration = Duration.none();
    private Time lastUpdate = Time.now();
    private final Time started = Time.now();
    private Duration runningTime = Duration.none();

    @Override
    public int fps() {
        return fps;
    }

    @Override
    public Duration updateDuration() {
        return updateDuration;
    }

    @Override
    public double delta() {
        return delta;
    }

    @Override
    public Duration runningTime() {
        return runningTime;
    }

    @Override
    public Time lastUpdate() {
        return lastUpdate;
    }

    @Override
    public long frameNumber() {
        return frameNumber;
    }

    public void trackUpdateCycle(final Duration duration) {
        final Time now = Time.now();
        final Duration timeBetweenUpdates = Duration.between(now, lastUpdate);
        lastUpdate = now;
        fps = (int) (NANOS_PER_SECOND / timeBetweenUpdates.nanos());
        final double maxUpdateFactor = fps <= CRITICAL_FPS_COUNT ? 0 : 1.0 / fps;
        delta = min(timeBetweenUpdates.nanos() * 1.0 / NANOS_PER_SECOND, maxUpdateFactor);
        updateDuration = duration;
        runningTime = Duration.between(started, lastUpdate);
        frameNumber++;
    }

}

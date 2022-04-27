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
    private double updateFactor = 0;
    private Duration updateDuration = Duration.zero();
    private Time lastUpdate = Time.now();
    private Time started = Time.now();
    private Duration runtimeDuration = Duration.zero();

    @Override
    public int framesPerSecond() {
        return fps;
    }

    @Override
    public Duration durationOfLastUpdate() {
        return updateDuration;
    }

    @Override
    public Duration durationSinceLastUpdate() {
        return Duration.since(lastUpdate);
    }

    public void trackUpdateCycle(Duration duration) {
        final Duration timeBetweenUpdates = Duration.since(lastUpdate);
        lastUpdate = Time.now();
        fps = (int) (NANOS_PER_SECOND / timeBetweenUpdates.nanos());
        final double maxUpdateFactor = fps <= CRITICAL_FPS_COUNT ? 0 : 1.0 / fps;
        updateFactor = min(timeBetweenUpdates.nanos() * 1.0 / NANOS_PER_SECOND, maxUpdateFactor);
        updateDuration = duration;
        runtimeDuration = Duration.between(started, lastUpdate);
        frameNumber++;
    }

    @Override
    public double updateFactor() {
        return updateFactor;
    }

    @Override
    public Duration durationOfRuntime() {
        return runtimeDuration;
    }

    @Override
    public Time timeOfLastUpdate() {
        return lastUpdate;
    }

    @Override
    public long frameNumber() {
        return frameNumber;
    }

}

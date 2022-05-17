package de.suzufa.screwbox.core.loop.internal;

import java.util.List;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.loop.GameLoop;
import de.suzufa.screwbox.core.loop.Metrics;

public class DefaultGameLoop implements GameLoop {

    private final DefaultMetrics metrics;
    private final List<Updatable> updatables;
    private boolean active = false;
    private int targetFps = 120;

    public DefaultGameLoop(final DefaultMetrics metrics, final List<Updatable> updatables) {
        this.metrics = metrics;
        this.updatables = updatables;
    }

    public void start() {
        if (active) {
            throw new IllegalStateException("game loop already started");
        }
        active = true;
        runGameLoop();
    }

    private void runGameLoop() {
        while (active) {
            if (needsUpdate()) {
                final Time beforeUpdate = Time.now();
                for (final var updatable : updatables) {
                    updatable.update();
                }
                metrics.trackUpdateCycle(Duration.since(beforeUpdate));
            } else {
                beNiceToCpu();
            }
        }
    }

    private void beNiceToCpu() {
        try {
            Thread.sleep(0, 750_000);
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void stop() {
        if (!active) {
            throw new IllegalStateException("game loop hast not been started yet");
        }
        this.active = false;
    }

    public boolean needsUpdate() {
        final double targetNanosPerUpdate = Time.NANOS_PER_SECOND * 1.0 / targetFps;
        final double nanosLastUpdate = metrics.durationOfLastUpdate().nanos();
        final double nanosSinceLastUpdate = metrics.durationSinceLastUpdate().nanos();
        return nanosSinceLastUpdate > targetNanosPerUpdate - (nanosLastUpdate * 1.5);
    }

    @Override
    public Metrics metrics() {
        return metrics;
    }

    @Override
    public GameLoop setTargetFps(final int targetFps) {
        if (targetFps <= 0) {
            throw new IllegalArgumentException("target fps must have a positive value");
        }
        this.targetFps = targetFps;
        return this;

    }

    @Override
    public int targetFps() {
        return targetFps;
    }

}

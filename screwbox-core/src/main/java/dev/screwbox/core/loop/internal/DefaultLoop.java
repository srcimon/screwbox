package dev.screwbox.core.loop.internal;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Time;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.utils.Validate;
import dev.screwbox.core.utils.internal.ThreadSupport;

import java.util.List;

public class DefaultLoop implements Loop {

    private static final int CRITICAL_FPS_COUNT = 30;
    private final List<Updatable> updatables;
    private double speed = 1;
    private int fps = 0;
    private long frameNumber;
    private double delta = 0;
    private boolean isRunning = false;
    private Duration updateDuration = Duration.none();
    private Time lastUpdate = Time.now();
    private Time startTime = Time.unset();
    private Duration runningTime = Duration.none();
    private boolean active = false;
    private int targetFps = 120;

    public DefaultLoop(final List<Updatable> updatables) {
        this.updatables = updatables;
    }

    public void start() {
        if (active) {
            throw new IllegalStateException("game loop already started");
        }
        active = true;
        startTime = Time.now();
        runGameLoop();
    }

    public void stop() {
        this.active = false;
    }

    @Override
    public double speed() {
        return speed;
    }

    @Override
    public void setSpeed(final double speed) {
        Validate.zeroOrPositive(speed, "speed must be positive");
        Validate.max(speed, 10.0, "speed cannot exceed 10.0");
        this.speed = speed;
    }

    @Override
    public Loop setTargetFps(final int targetFps) {
        Validate.range(targetFps, 60, Integer.MAX_VALUE, "target fps must be at least 60");
        this.targetFps = targetFps;
        return this;

    }

    @Override
    public Loop unlockFps() {
        return setTargetFps(Integer.MAX_VALUE);
    }

    @Override
    public int targetFps() {
        return targetFps;
    }

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
    public Time time() {
        return lastUpdate;
    }

    @Override
    public long frameNumber() {
        return frameNumber;
    }

    private void runGameLoop() {
        isRunning = true;
        try {
            while (active) {
                if (needsUpdate()) {
                    final Time beforeUpdate = Time.now();
                    for (final var updatable : updatables) {
                        updatable.update();
                    }
                    trackUpdateCycle(beforeUpdate);
                } else {
                    ThreadSupport.beNiceToCpu();
                }
            }
        } finally {
            isRunning = false;
        }
    }

    private boolean needsUpdate() {
        final double targetNanosPerUpdate = Time.Unit.SECONDS.nanos() * 1.0 / targetFps;
        final double nanosSinceLastUpdate = Duration.since(lastUpdate).nanos();
        return nanosSinceLastUpdate > targetNanosPerUpdate - (updateDuration.nanos() * 1.4);
    }

    private void trackUpdateCycle(final Time beforeUpdate) {
        final Time now = Time.now();
        final Duration timeBetweenUpdates = Duration.between(now, lastUpdate);
        lastUpdate = now;
        fps = (int) (Time.Unit.SECONDS.nanos() / timeBetweenUpdates.nanos());
        final double maxUpdateFactor = fps <= CRITICAL_FPS_COUNT ? 0.005 : 1.0 / fps;
        delta = Math.min(timeBetweenUpdates.nanos() * 1.0 / Time.Unit.SECONDS.nanos(), maxUpdateFactor) * speed;
        updateDuration = Duration.between(now, beforeUpdate);
        runningTime = Duration.between(startTime, lastUpdate);
        frameNumber++;
    }

    @Override
    public Time startTime() {
        return startTime;
    }

    public void awaitTermination() {
        while (isRunning) {
            ThreadSupport.beNiceToCpu();
        }
    }

}

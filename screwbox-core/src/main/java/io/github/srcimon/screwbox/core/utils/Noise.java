package io.github.srcimon.screwbox.core.utils;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;

import java.io.Serializable;
import java.util.Random;

import static java.util.Objects.isNull;

public class Noise implements Serializable {

    private static final Random RANDOM = new Random();

    private final Duration minInterval;
    private final Duration maxInterval;
    private Time intervalStart;
    private Time intervalEnd;
    private double lastValue = 0;
    private double targetValue = RANDOM.nextDouble(-1, 1);

    public static Noise fixedInterval(final Duration interval) {
        return new Noise(interval, interval);
    }

    public static Noise variableInterval(final Duration interval) {
        final long nanoDeviation = Math.round(interval.nanos() * Percent.half().value());
        final Duration minDeviation = Duration.ofNanos(-1 * nanoDeviation);
        final Duration maxDeviation = Duration.ofNanos(nanoDeviation);
        return new Noise(interval.add(minDeviation), interval.add(maxDeviation));
    }

    private Noise(final Duration minInterval, final Duration maxInterval) {
        this.minInterval = minInterval;
        this.maxInterval = maxInterval;
    }

    public double value(final Time time) {
        if (isNull(intervalStart) || time.isAfter(intervalEnd)) {
            intervalStart = time;
            intervalEnd = time.plus(calculateNextInterval());
            lastValue = targetValue;
            targetValue = targetValue < 0 ? RANDOM.nextDouble(0, 1) : RANDOM.nextDouble(-1, 0);
        }

        final var percent = Percent.of(1.0 * (time.nanos() - intervalStart.nanos()) / (intervalEnd.nanos() - intervalStart.nanos()));
        final var dist = lastValue - targetValue;
        final var speedAtDist = MathUtil.clamp(0, Math.sin(percent.value() * Math.PI) + 1, 1);
        return (dist * percent.value() * speedAtDist) - lastValue;
    }

    private Duration calculateNextInterval() {
        return minInterval.equals(maxInterval)
                ? minInterval
                : Duration.ofNanos(RANDOM.nextLong(minInterval.nanos(), maxInterval.nanos()));
    }

}

package io.github.srcimon.screwbox.core.utils;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;

import java.util.Random;

import static java.util.Objects.isNull;

public class Lurk {

    private static final Random RANDOM = new Random();

    private final Duration minInterval;
    private final Duration maxInterval;
    private Time intervalStart;
    private Time intervalEnd;
    private double targetValue = RANDOM.nextDouble(-1, 1);

    public static Lurk fixedInterval(final Duration interval) {
        return intervalWithDeviation(interval, Percent.min());
    }

    public static Lurk intervalWithDeviation(final Duration interval, final Percent intervalDeviation) {
        long nanoDeviation = Math.round(interval.nanos() * intervalDeviation.value());
        return new Lurk(interval.addNanos(-nanoDeviation), interval.addNanos(nanoDeviation));
    }

    private Lurk(final Duration minInterval, final Duration maxInterval) {
        this.minInterval = minInterval;
        this.maxInterval = maxInterval;
    }

    public double value(final Time time) {
        if (isNull(intervalStart) || time.isAfter(intervalEnd)) {
            intervalStart = time;
            intervalEnd = time.plus(calcNextInterval());
            targetValue = targetValue < 0 ? RANDOM.nextDouble(0, 1) : RANDOM.nextDouble(-1, 0);
        }

        var percentOfWay = Percent.of(1.0 * (time.nanos() - intervalStart.nanos()) / (intervalEnd.nanos() - intervalStart.nanos()));
        return Math.sin(Math.PI / 2 * percentOfWay.value()) * targetValue;
    }

    private Duration calcNextInterval() {
        return minInterval.equals(maxInterval)
                ? minInterval
                : Duration.ofNanos(RANDOM.nextLong(minInterval.nanos(), maxInterval.nanos()));
    }

}

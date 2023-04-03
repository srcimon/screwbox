package io.github.simonbas.screwbox.core.test;

import io.github.simonbas.screwbox.core.Duration;
import io.github.simonbas.screwbox.core.Time;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public final class TestUtil {

    private TestUtil() {
    }

    public static void shutdown(final ExecutorService executor) {
        try {
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void await(final Supplier<Boolean> condition, final Duration timeout) {
        final Time start = Time.now();
        while (!condition.get()) {
            if (Duration.since(start).isAtLeast(timeout)) {
                throw new AssertionError("condition not met in timeout");
            }
        }
    }
}

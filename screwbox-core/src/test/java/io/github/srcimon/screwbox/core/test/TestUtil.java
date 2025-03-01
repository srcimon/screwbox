package io.github.srcimon.screwbox.core.test;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.graphics.Frame;

import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

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

    public static void sleep(final Duration duration) {
        try {
            Thread.sleep(duration.milliseconds());
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

    public static void verifyIsSameImage(final Image result, final String file) {
        Frame resultFrame = io.github.srcimon.screwbox.core.graphics.Frame.fromImage(result);
        Frame fileFrame = Frame.fromFile( file);
        assertThat(fileFrame.listPixelDifferences(resultFrame)).isEmpty();
    }
}

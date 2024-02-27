package io.github.srcimon.screwbox.core.test;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.graphics.Frame;
import io.github.srcimon.screwbox.core.graphics.internal.ImageUtil;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
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

    public static void exportPng(final Frame frame, final String name) {
        try {
            ImageIO.write(ImageUtil.toBufferedImage(frame.image()), "png", new File(name + ".png"));
        } catch (IOException e) {
            throw new IllegalStateException("could not export png", e);
        }
    }

}

package dev.screwbox.core.test;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Time;
import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.utils.Validate;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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
        Frame resultFrame = Frame.fromImage(result);
        Frame fileFrame = Frame.fromFile(file);
        assertThat(fileFrame.hasIdenticalPixels(resultFrame)).isTrue();
    }

    public static void times(final int count, final Runnable runnable) {
        Validate.positive(count, "count must be positive");
        for (int i = 0; i < count; i++) {
            runnable.run();
        }
    }

    public static <T extends Serializable> T roundTripSerialization(final T pbject) {
        final byte[] serialized = serialize(pbject);
        return deserialize(serialized);
    }

    private static <T extends Serializable> T deserialize(final byte[] object) {
        try (final var inputStream = new ByteArrayInputStream(object)) {
            try (final var objectInputStream = new ObjectInputStream(inputStream)) {
                return (T) objectInputStream.readObject();
            }
        } catch (final IOException | ClassNotFoundException e) {
            throw new IllegalStateException("could not deserialize data", e);
        }
    }

    private static <T extends Serializable> byte[] serialize(T object) {
        try (final var outputStream = new ByteArrayOutputStream()) {
            try (final var objectOutputStream = new ObjectOutputStream(outputStream)) {
                objectOutputStream.writeObject(object);
                return outputStream.toByteArray();
            }
        } catch (final IOException e) {
            throw new IllegalStateException("could not serialize object", e);
        }
    }
}

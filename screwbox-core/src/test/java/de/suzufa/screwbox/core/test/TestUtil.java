package de.suzufa.screwbox.core.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public final class TestUtil {

    private TestUtil() {
    }

    public static void shutdown(ExecutorService executor) {
        try {
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

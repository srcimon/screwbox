package dev.screwbox.core.utils.internal;

public class ThreadSupport {

    private ThreadSupport() {
    }

    public static void beNiceToCpu() {
        try {
            Thread.sleep(0, 750_000);
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

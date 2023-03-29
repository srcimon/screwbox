package io.github.simonbas.screwbox.core;

import io.github.simonbas.screwbox.core.loop.Loop;

class WarmUpIndicator {

    public static final Duration WANTED_UPDATE_TIME = Duration.ofMillis(1);
    public static final Duration TIMEOUT = Duration.ofSeconds(10);
    public static final int TEST_COUNT = 20;

    private final Loop loop;

    private int warmedUpCount = 0;
    private boolean isWarmedUp = false;

    WarmUpIndicator(final Loop loop) {
        this.loop = loop;
    }

    boolean isWarmedUp() {
        if (isWarmedUp) {
            return true;
        }

        if (warmedUpCount >= TEST_COUNT) {
            isWarmedUp = true;
            return true;
        }
        if (loop.runningTime().isAtLeast(TIMEOUT)) {
            isWarmedUp = true;
            return true;
        }
        warmedUpCount++;

        if (loop.updateDuration().isAtLeast(WANTED_UPDATE_TIME)) {
            warmedUpCount = 0;
        }
        return false;
    }
}

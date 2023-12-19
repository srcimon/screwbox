package io.github.srcimon.screwbox.core;

import io.github.srcimon.screwbox.core.log.Log;
import io.github.srcimon.screwbox.core.loop.Loop;

class WarmUpIndicator {

    public static final Duration WANTED_UPDATE_TIME = Duration.ofMillis(1);
    public static final Duration TIMEOUT = Duration.ofSeconds(10);
    public static final int TEST_COUNT = 20;

    private final Loop loop;
    private final Log log;

    private int warmedUpCount = 0;
    private boolean isWarmedUp = false;

    WarmUpIndicator(final Loop loop, Log log) {
        this.loop = loop;
        this.log = log;
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
            log.warn("warmup timed out, starting anyway");
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

package dev.screwbox.core.utils;

import java.util.Random;

public class FastRandom {

    private final long seed;

    public FastRandom(long seed) {
        this.seed = seed;
    }

    private static final boolean[] BOOLS = new boolean[256];

    static {
        var r = new Random(123);
        for (int i = 0; i < BOOLS.length; i++) {
            BOOLS[i] = r.nextBoolean();
        }
    }

    int index = 0;

    public boolean nextBoolean() {
        index++;
        return BOOLS[Math.abs((int) (seed + index)) % BOOLS.length];
    }
}

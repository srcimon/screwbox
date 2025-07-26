package dev.screwbox.core.utils;

import java.util.Random;

/**
 * A non threadsafe {@link Random} alternative used for fast random value generation.
 */
public final class FastRandom {

    private static final boolean[] HASHES = createInitialHashes();

    private final long seed;
    private int index = 0;

    public FastRandom(final long seed) {
        this.seed = seed;
    }

    //TODO Documentation and tests
    //TODO mention in changelog
    public boolean nextBoolean() {
        index++;
        return HASHES[Math.abs((int) (seed + index)) % HASHES.length];
    }

    private static boolean[] createInitialHashes() {
        final boolean[] hashes = new boolean[1024];
        var seededRandom = new Random(123);
        for (int i = 0; i < hashes.length; i++) {
            hashes[i] = seededRandom.nextBoolean();
        }
        return hashes;
    }
}

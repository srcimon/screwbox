package dev.screwbox.core.utils;

import java.util.Random;
import java.util.UUID;

/**
 * A non threadsafe {@link Random} alternative used for fast random value generation.
 *
 * @since 3.6.0
 */
public final class FastRandom {

    private static final boolean[] HASHES = createInitialHashes();
    private final Random random;
    private final long seed;
    private int index = 0;

    /**
     * New instance using random seed.
     */
    public FastRandom() {
        this(new Random().nextLong());
    }

    /**
     * New instance using the specified seed to create reproducible results.
     */
    public FastRandom(final long seed) {
        this.seed = seed;
        this.random = new Random(seed);
    }

    /**
     * Creates a random UUID. Might be significantly less unique than {@link UUID#randomUUID()} but is much faster
     * 2 to 10 times depending on coffeenication of the JVM.
     *
     * @since 3.12.0
     */
    public UUID createUUID() {
        return new UUID(random.nextLong(), random.nextLong());
    }

    /**
     * Returns the next pseudo random boolean value. Is two times faster as {@link Random#nextBoolean()} at the cost
     * of less randomness and no thread safety.
     */
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

package io.github.srcimon.screwbox.core.graphics;

public record SplitScreenOptions(int screenCount) {

    public SplitScreenOptions {
        if (screenCount != 2) {
            throw new IllegalArgumentException("TODO implement more than one additional split screen");
        }
    }

    public static SplitScreenOptions screenCount(final int screenCount) {
        return new SplitScreenOptions(screenCount);
    }
}

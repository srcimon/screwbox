package io.github.srcimon.screwbox.core.graphics;

public record SplitScreenOptions(int screenCount) {

    public SplitScreenOptions {
 //TODO validations
    }

    public static SplitScreenOptions screenCount(final int screenCount) {
        return new SplitScreenOptions(screenCount);
    }
}

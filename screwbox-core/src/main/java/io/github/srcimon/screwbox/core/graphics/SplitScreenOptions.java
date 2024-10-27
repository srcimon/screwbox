package io.github.srcimon.screwbox.core.graphics;

public record SplitScreenOptions(int screenCount, Arangement arangement) {

    public enum Arangement {
        HORIZONTAL,
        VERTICAL
    }
    public SplitScreenOptions {
 //TODO validations
    }

    public static SplitScreenOptions screenCount(final int screenCount) {
        return new SplitScreenOptions(screenCount, Arangement.HORIZONTAL);
    }

    public SplitScreenOptions arangement(final Arangement arangement) {
        return new SplitScreenOptions(screenCount, arangement);
    }
}

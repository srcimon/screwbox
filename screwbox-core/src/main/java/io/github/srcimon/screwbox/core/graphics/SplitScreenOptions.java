package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.graphics.drawoptions.LineDrawOptions;

public record SplitScreenOptions(int screenCount, Arangement arangement, LineDrawOptions borderOptions) {

    public enum Arangement {
        HORIZONTAL,
        VERTICAL
    }
    public SplitScreenOptions {
 //TODO validations
    }

    public static SplitScreenOptions screenCount(final int screenCount) {
        return new SplitScreenOptions(screenCount, Arangement.HORIZONTAL, LineDrawOptions.color(Color.BLACK).strokeWidth(4));
    }


    public SplitScreenOptions arangement(final Arangement arangement) {
        return new SplitScreenOptions(screenCount, arangement, borderOptions);
    }

    public SplitScreenOptions borderOptions(final LineDrawOptions borderOptions) {
        return new SplitScreenOptions(screenCount, arangement, borderOptions);
    }
}

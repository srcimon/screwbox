package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.graphics.drawoptions.LineDrawOptions;
import io.github.srcimon.screwbox.core.graphics.layouts.HorizontalLayout;

public record SplitScreenOptions(int screenCount, LineDrawOptions border, ViewportLayout layout) {

    public SplitScreenOptions {
 //TODO validations
    }
//TODO SplitScreenOptions.horizontal(3)

    public static SplitScreenOptions horizontal(final int screenCount) {
        return new SplitScreenOptions(screenCount, LineDrawOptions.color(Color.BLACK).strokeWidth(4), new HorizontalLayout());
    }

    public SplitScreenOptions layout(final ViewportLayout layout) {
        return new SplitScreenOptions(screenCount,  border, layout);
    }

    public SplitScreenOptions border(final LineDrawOptions borderOptions) {
        return new SplitScreenOptions(screenCount,  borderOptions, layout);
    }
}

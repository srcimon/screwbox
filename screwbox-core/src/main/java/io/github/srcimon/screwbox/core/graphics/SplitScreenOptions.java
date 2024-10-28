package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.graphics.drawoptions.LineDrawOptions;
import io.github.srcimon.screwbox.core.graphics.layouts.HorizontalLayout;
import io.github.srcimon.screwbox.core.graphics.layouts.TableLayout;
import io.github.srcimon.screwbox.core.graphics.layouts.VerticalLayout;
import io.github.srcimon.screwbox.core.utils.Validate;

public record SplitScreenOptions(int screenCount, LineDrawOptions border, ViewportLayout layout) {

    public SplitScreenOptions {
        Validate.positive(screenCount, "split screen must have at least one screen");
        //TODO validations
    }

    public static SplitScreenOptions horizontal(final int screenCount) {
        return new SplitScreenOptions(screenCount, LineDrawOptions.color(Color.BLACK).strokeWidth(4), new HorizontalLayout());
    }

    public static SplitScreenOptions vertical(final int screenCount) {
        return new SplitScreenOptions(screenCount, LineDrawOptions.color(Color.BLACK).strokeWidth(4), new VerticalLayout());
    }

    public static SplitScreenOptions squares(final int screenCount) {
        return new SplitScreenOptions(screenCount, LineDrawOptions.color(Color.BLACK).strokeWidth(4), new TableLayout());
    }

    public SplitScreenOptions layout(final ViewportLayout layout) {
        return new SplitScreenOptions(screenCount, border, layout);
    }

    public SplitScreenOptions border(final LineDrawOptions borderOptions) {
        return new SplitScreenOptions(screenCount, borderOptions, layout);
    }
}

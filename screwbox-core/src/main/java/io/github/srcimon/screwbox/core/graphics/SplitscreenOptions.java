package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.graphics.drawoptions.LineDrawOptions;
import io.github.srcimon.screwbox.core.graphics.layouts.HorizontalLayout;
import io.github.srcimon.screwbox.core.graphics.layouts.TableLayout;
import io.github.srcimon.screwbox.core.graphics.layouts.VerticalLayout;
import io.github.srcimon.screwbox.core.utils.Validate;

//TODO add javadoc to all methods
//TODO add tests
public record SplitscreenOptions(int viewportCount, LineDrawOptions border, ViewportLayout layout) {

    public SplitscreenOptions {
        Validate.positive(viewportCount, "split screen must have at least one viewport");
        if (viewportCount > 64) {
            throw new IllegalArgumentException("split screen supports only up to 64 viewports (what is your monitor like?)");
        }
    }

    public static SplitscreenOptions viewports(final int screenCount) {
        return new SplitscreenOptions(screenCount, LineDrawOptions.color(Color.BLACK).strokeWidth(4), new HorizontalLayout());
    }

    public SplitscreenOptions layout(final ViewportLayout layout) {
        return new SplitscreenOptions(viewportCount, border, layout);
    }

    public SplitscreenOptions verticalLayout() {
        return layout(new VerticalLayout());
    }

    public SplitscreenOptions tableLayout() {
        return layout(new TableLayout());
    }

    public SplitscreenOptions borders(final LineDrawOptions borderOptions) {
        return new SplitscreenOptions(viewportCount, borderOptions, layout);
    }

    /**
     * Split screen {@link Viewport viewports} will be drawn without any borders.
     *
     * @since 2.6.0
     */
    public SplitscreenOptions noBorders() {
        return new SplitscreenOptions(viewportCount, null, layout);
    }

}

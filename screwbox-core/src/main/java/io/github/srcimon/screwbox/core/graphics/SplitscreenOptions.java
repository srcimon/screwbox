package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.graphics.drawoptions.LineDrawOptions;
import io.github.srcimon.screwbox.core.graphics.layouts.HorizontalLayout;
import io.github.srcimon.screwbox.core.graphics.layouts.TableLayout;
import io.github.srcimon.screwbox.core.graphics.layouts.VerticalLayout;
import io.github.srcimon.screwbox.core.utils.Validate;

/**
 * Configures split screen mode.
 *
 * @param viewportCount the number of {@link Viewport viewports} present in the split screen
 * @param borders       the border style used to separate the {@link Viewport viewports}
 * @param layout        the layout wich is applied to calculate the size and position of the {@link Viewport viewports}
 * @since 2.5.0
 */
public record SplitscreenOptions(int viewportCount, LineDrawOptions borders, ViewportLayout layout, int padding) {
//TODO remove borders options from options
    public SplitscreenOptions {
        Validate.positive(viewportCount, "split screen must have at least one viewport");
        Validate.zeroOrPositive(padding, "padding must be positive");//TODO test

        if (viewportCount > 64) {
            throw new IllegalArgumentException("split screen supports only up to 64 viewports (what is your monitor like?)");
        }
    }

    /**
     * Specify the number of {@link Viewport viewports}. Maximum number is 64 {@link Viewport viewports} for really large
     * screens.
     *
     * @since 2.5.0
     */
    public static SplitscreenOptions viewports(final int screenCount) {
        return new SplitscreenOptions(screenCount, LineDrawOptions.color(Color.BLACK).strokeWidth(4), new HorizontalLayout(), 0);
    }

    /**
     * Specify custom layout for position and size of the {@link Viewport viewports}.
     *
     * @since 2.5.0
     */
    public SplitscreenOptions layout(final ViewportLayout layout) {
        return new SplitscreenOptions(viewportCount, borders, layout, padding);
    }

    /**
     * Use vertical layout for position and size of the {@link Viewport viewports}.
     *
     * @since 2.5.0
     */
    public SplitscreenOptions verticalLayout() {
        return layout(new VerticalLayout());
    }

    /**
     * Use table layout for position and size of the {@link Viewport viewports}.
     *
     * @since 2.5.0
     */
    public SplitscreenOptions tableLayout() {
        return layout(new TableLayout());
    }

    /**
     * Specify the boder style between the split screen {@link Viewport viewports}.
     *
     * @since 2.5.0
     */
    public SplitscreenOptions borders(final LineDrawOptions borderOptions) {
        return new SplitscreenOptions(viewportCount, borderOptions, layout, padding);
    }

    /**
     * Split screen {@link Viewport viewports} will be drawn without any borders.
     *
     * @since 2.6.0
     */
    public SplitscreenOptions noBorders() {
        return new SplitscreenOptions(viewportCount, null, layout, padding);
    }

    //TODO javadoc and test
    public SplitscreenOptions padding(final int padding) {
        return new SplitscreenOptions(viewportCount, borders, layout, padding);
    }
}
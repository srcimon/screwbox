package dev.screwbox.core.graphics;

import dev.screwbox.core.graphics.layouts.HorizontalLayout;
import dev.screwbox.core.graphics.layouts.TableLayout;
import dev.screwbox.core.graphics.layouts.VerticalLayout;
import dev.screwbox.core.utils.Validate;

/**
 * Configures split screen mode. {@link Color} of padding between {@link Viewport viewports} can
 * be changed via {@link GraphicsConfiguration#setBackgroundColor(Color)}.
 *
 * @param viewportCount the number of {@link Viewport viewports} present in the split screen
 * @param layout        the layout which is applied to calculate the size and position of the {@link Viewport viewports}
 * @since 2.5.0
 */
public record SplitScreenOptions(int viewportCount, ViewportLayout layout, int padding) {

    public SplitScreenOptions {
        Validate.positive(viewportCount, "split screen must have at least one viewport");
        Validate.max(viewportCount, 64, "split screen supports only up to 64 viewports (what is your monitor like?)");

        Validate.zeroOrPositive(padding, "padding must be positive");
        Validate.max(padding, 32, "padding has max value of 32");
    }

    /**
     * Specify the number of {@link Viewport viewports}. Maximum number is 64 {@link Viewport viewports} for really large
     * screens.
     *
     * @since 2.5.0
     */
    public static SplitScreenOptions viewports(final int screenCount) {
        return new SplitScreenOptions(screenCount, new HorizontalLayout(), 4);
    }

    /**
     * Specify custom layout for position and size of the {@link Viewport viewports}.
     *
     * @since 2.5.0
     */
    public SplitScreenOptions layout(final ViewportLayout layout) {
        return new SplitScreenOptions(viewportCount, layout, padding);
    }

    /**
     * Use vertical layout for position and size of the {@link Viewport viewports}.
     *
     * @since 2.5.0
     */
    public SplitScreenOptions verticalLayout() {
        return layout(new VerticalLayout());
    }

    /**
     * Use table layout for position and size of the {@link Viewport viewports}.
     *
     * @since 2.5.0
     */
    public SplitScreenOptions tableLayout() {
        return layout(new TableLayout());
    }

    /**
     * Sets padding between {@link Viewport viewports} to zero. Default is 4.
     *
     * @since 2.6.0
     */
    public SplitScreenOptions noPadding() {
        return padding(0);
    }

    /**
     * Sets padding between {@link Viewport viewports} to the specified value. Default is 4.
     * Max value is 32.
     *
     * @since 2.6.0
     */
    public SplitScreenOptions padding(final int padding) {
        return new SplitScreenOptions(viewportCount, layout, padding);
    }
}
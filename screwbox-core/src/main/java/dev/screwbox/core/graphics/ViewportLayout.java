package dev.screwbox.core.graphics;

/**
 * This layout can be used to calculate the position and size of a {@link Viewport} that is present in a split screen.
 *
 * @see Graphics#enableSplitScreenMode(SplitScreenOptions)
 */
public interface ViewportLayout {

    /**
     * Calculate the specific bounds on the screen for a {@link Viewport}.
     *
     * @param padding the padding between the {@link Viewport viewports}
     * @param index   the value of the viewport in the list of {@link Graphics#viewports()} (starts at 0)
     * @param count   the total count of {@link Viewport viewports} of the split screen
     * @param bounds  the surrounding bounds of the {@link Screen}
     */
    ScreenBounds calculateBounds(int index, int count, int padding, ScreenBounds bounds);

}

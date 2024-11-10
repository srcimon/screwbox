package io.github.srcimon.screwbox.core.graphics;

/**
 * This layout can be used to calculate the position and size of a {@link Viewport} that is present in a split screen.
 *
 * @see Graphics#enableSplitscreenMode(SplitscreenOptions)
 */
public interface ViewportLayout {

    /**
     * Calculate the specific bounds on the screen for a {@link Viewport}.
     *
     * @param index  the index of the viewport in the list of {@link Graphics#viewports()} (starts at 0)
     * @param count  the total count of {@link Viewport viewports} of the split screen
     * @param bounds the surrounding bounds of the {@link Screen}
     * @param paddings the padding between the {@link Viewport viewports}
     */
    ScreenBounds calculateBounds(int index, int count, ScreenBounds bounds, int padding);

}

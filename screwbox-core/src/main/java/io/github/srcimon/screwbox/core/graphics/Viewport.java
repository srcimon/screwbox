package io.github.srcimon.screwbox.core.graphics;

/**
 * Rendering always happens within the {@link Viewport}. Without any other specification the {@link Viewport} is
 * identical with the {@link Screen}.
 */
//TODO: document how to change the viewport
public interface Viewport extends Sizeable {

    /**
     * Returns the left upper edge of the {@link Viewport}.
     */
    Offset offset();

    /**
     * Returns the center of the {@link Viewport}.
     */
    Offset center();

    ScreenBounds bounds();
}

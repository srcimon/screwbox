package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;

/**
 * Get information abound the {@link Camera} like {@link Camera#position()}. Change {@link Camera} settings like {@link Camera#zoom()}.
 */
public interface Camera {

    /**
     * Moves the camera within the given {@link Bounds}.
     *
     * @return the actual camera position after movement is applied
     */
    Vector moveWithinVisualBounds(Vector delta, Bounds bounds);

    /**
     * Updates the camera zoom nearly to the given value. The actual zoom value may
     * be slightly different to avoid graphic glitches because of floating point
     * imprecisions. The actual zoom value is returned.
     *
     * @param zoom the zoom value that should be applied
     * @return the zoom value that was applied
     * @see #updateZoomRelative(double)
     */
    double updateZoom(double zoom);

    /**
     * Sets the camera position.
     */
    Camera updatePosition(Vector position);

    /**
     * Updates the camera zoom nearly by the given value. The actual zoom value may
     * be slightly different to avoid graphic glitches because of floating point
     * imprecisions. The actual zoom value is returned.
     *
     * @see #updateZoom(double)
     */
    double updateZoomRelative(double delta);

    /**
     * Moves the camera position by the given {@link Vector}.
     */
    default Camera move(final Vector delta) {
        return updatePosition(position().add(delta));
    }

    /**
     * Shakes the {@link Camera} with the given {@link ShakeOptions}. Shaking has no effect
     * on {@link Camera#position()}. Shake only afflicts {@link Camera#focus()} and the rendering of {@link World}.
     *
     * @see #stopShaking()
     * @see #isShaking()
     */
    Camera shake(ShakeOptions options);

    /**
     * Stops any {@link Camera} if there is currently shake in progress. Does nothing if {@link Camera} is not shaking.
     *
     * @see #shake(ShakeOptions)
     * @see #isShaking()
     */
    Camera stopShaking();

    /**
     * Returns {@true} if {@link Camera} is curently shaking.
     *
     * @see #shake(ShakeOptions)
     * @see #stopShaking()
     */
    boolean isShaking();

    /**
     * Restricts zooming to the given range. Default min zoom is 0.5 and max is 10.
     */
    Camera updateZoomRestriction(double min, double max);

    Vector position();

    Vector focus();

    /**
     * Returns the currently used camera zoom.
     */
    double zoom();

    double minZoom();

    double maxZoom();
}

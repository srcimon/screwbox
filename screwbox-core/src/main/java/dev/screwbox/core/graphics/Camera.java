package dev.screwbox.core.graphics;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Rotation;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.options.CameraShakeOptions;

/**
 * Get information abound the {@link Camera} like {@link Camera#position()}. Change {@link Camera} settings like {@link Camera#zoom()}.
 */
public interface Camera {

    /**
     * Moves the camera within the specified {@link Bounds}.
     *
     * @return the actual camera position after movement is applied
     */
    Vector moveWithinVisualBounds(Vector delta, Bounds bounds);

    /**
     * Updates the camera zoom nearly to the specified value. The actual zoom value may
     * be slightly different to avoid graphic glitches because of floating point
     * imprecision. The actual zoom value is returned.
     *
     * @param zoom the zoom value that should be applied
     * @return the zoom value that was applied
     * @see #changeZoomBy(double)
     */
    double setZoom(double zoom);

    /**
     * Sets the camera position.
     */
    Camera setPosition(Vector position);

    /**
     * Updates the camera zoom nearly by the specified value. The actual zoom value may
     * be slightly different to avoid graphic glitches because of floating point
     * imprecision. The actual zoom value is returned.
     *
     * @see #setZoom(double)
     */
    double changeZoomBy(double delta);

    /**
     * Moves the camera position by the given {@link Vector}.
     */
    default Camera move(final Vector delta) {
        return setPosition(position().add(delta));
    }

    /**
     * Shakes the {@link Camera} with the specified {@link CameraShakeOptions}. Shaking has no effect
     * on {@link Camera#position()}. Shake only afflicts {@link Camera#focus()} and the rendering of {@link World}.
     *
     * @see #stopShaking()
     * @see #isShaking()
     */
    Camera shake(CameraShakeOptions options);

    /**
     * Stops any {@link Camera} if there is currently shake in progress. Does nothing if {@link Camera} is not shaking.
     *
     * @see #shake(CameraShakeOptions)
     * @see #isShaking()
     */
    Camera stopShaking();

    /**
     * Returns {@code true} if {@link Camera} is currently shaking.
     *
     * @see #shake(CameraShakeOptions)
     * @see #stopShaking()
     */
    boolean isShaking();

    /**
     * Restricts zooming to the given range. Default min zoom is 0.5 and max is 10.
     */
    Camera setZoomRestriction(double min, double max);

    /**
     * Returns the position of the {@link Camera}.
     */
    Vector position();

    /**
     * Returns the focus of the {@link Camera}. The value differs from {@link #position()} while the
     * {@link Camera} {@link #isShaking()}.
     */
    Vector focus();

    /**
     * Returns the currently used camera zoom.
     */
    double zoom();

    /**
     * Returns the minimum {@link #zoom()} that can be set.
     *
     * @see #setZoomRestriction(double, double)
     */
    double minZoom();

    /**
     * Returns the maximum {@link #zoom()} that can be set.
     *
     * @see #setZoomRestriction(double, double)
     */
    double maxZoom();

    Rotation swing();
}

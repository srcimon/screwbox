package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.graphics.drawoptions.CameraShakeOptions;
import io.github.srcimon.screwbox.core.window.Window;

import java.util.Optional;

/**
 * Access drawing operations on the game screen.
 *
 * @see World
 */
public interface Screen extends Canvas {

    /**
     * Sets the rotation of the {@link Screen}. This is a very limited feature resulting in quite some frame drop and
     * does move rendered area outside of the game {@link Window}.
     */
    Screen setRotation(Rotation rotation);

    /**
     * Returns the current rotation of the {@link Screen} without current {@link #shake()}.
     *
     * @see #setRotation(Rotation)
     * @see #shake()
     * @see #absoluteRotation()
     */
    Rotation rotation();

    /**
     * Returns current shake of the {@link Screen} without current {@link #rotation()}. Shake is also a {@link Rotation}
     * that is applied via {@link Camera#shake(CameraShakeOptions)}.
     *
     * @see #setRotation(Rotation)
     * @see #rotation()
     * @see #absoluteRotation()
     */
    Rotation shake();

    /**
     * Returns the current rotation of the {@link Screen} including the current {@link #shake()}.
     *
     * @see #setRotation(Rotation)
     * @see #shake()
     * @see #rotation()
     */
    default Rotation absoluteRotation() {
        return rotation().add(shake());
    }

    /**
     * Returns the position of the {@link Screen} relative to the monitor.
     */
    Offset position();

    /**
     * Takes a sceenshot of the whole {@link Screen}. This operation is very slow and will propably cause a small lag.
     * The screenshot may also include other applications that are in front of your game screen.
     */
    Sprite takeScreenshot();

    /**
     * Returns the last taken screenshot. Will be empty if no screenshot has been taken.
     */
    Optional<Sprite> lastScreenshot();
}

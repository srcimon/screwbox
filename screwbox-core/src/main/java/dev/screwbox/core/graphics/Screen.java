package dev.screwbox.core.graphics;

import dev.screwbox.core.Rotation;
import dev.screwbox.core.graphics.options.CameraShakeOptions;
import dev.screwbox.core.window.Window;

import java.util.Optional;

/**
 * Access drawing operations on the game {@link Screen}.
 *
 * @see World
 */
public interface Screen extends Sizeable {

    /**
     * Restricts the drawing {@link Canvas} to the specified {@link ScreenBounds}. This stops the canvas from being
     * resized when resolution changes.
     *
     * @see #resetCanvasBounds()
     * @see Graphics#canvas()
     */
    Screen setCanvasBounds(ScreenBounds bounds);

    /**
     * Resets previously set canvas bounds. Automatically changes canvas bounds with resolution again.
     *
     * @see #setCanvasBounds(ScreenBounds)
     * @see Graphics#canvas()
     */
    Screen resetCanvasBounds();

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
     * Takes a screenshot of the whole {@link Screen}. This operation is very slow and will probably cause a small lag.
     * The screenshot may also include other applications that are in front of your game screen.
     */
    Sprite takeScreenshot();

    /**
     * Returns the last taken screenshot. Will be empty if no screenshot has been taken.
     */
    Optional<Sprite> lastScreenshot();
}

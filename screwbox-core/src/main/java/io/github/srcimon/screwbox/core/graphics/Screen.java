package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.graphics.drawoptions.CameraShakeOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.LineDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.RectangleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteFillOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SystemTextDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.TextDrawOptions;
import io.github.srcimon.screwbox.core.window.Window;

import javax.swing.text.View;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Access drawing operations on the game screen.
 *
 * @see World
 */
public interface Screen extends Sizeable {

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
     * Returns the {@link Size} of the {@link Screen}.
     */
    Size size();

    /**
     * Returns the center position of the {@link Screen}.
     */
    Offset center();

    /**
     * Fills the whole {@link Screen} with the given {@link Color}.
     */
    Screen fillWith(Color color);

    /**
     * Draws a rectangle on the screen using the given {@link ScreenBounds} and {@link RectangleDrawOptions}.
     *
     * @see #drawRectangle(ScreenBounds, RectangleDrawOptions)
     */
    Screen drawRectangle(final Offset origin, final Size size, final RectangleDrawOptions options);

    /**
     * Draws a rectangle on the {@link Screen} using the given {@link ScreenBounds} and {@link RectangleDrawOptions}.
     *
     * @see #drawRectangle(Offset, Size, RectangleDrawOptions)
     */
    default Screen drawRectangle(final ScreenBounds bounds, final RectangleDrawOptions options) {
        return drawRectangle(bounds.offset(), bounds.size(), options);
    }

    /**
     * Takes a sceenshot of the whole {@link Screen}. This operation is very slow and will propably cause a small lag.
     * The screenshot may also include other applications that are in front of your game screen.
     */
    Sprite takeScreenshot();

    /**
     * Draws a line on the {@link Screen} using the given {@link Offset}s and {@link LineDrawOptions}.
     */
    Screen drawLine(Offset from, Offset to, LineDrawOptions options);

    /**
     * Draws a circle on the {@link Screen} using the given position and {@link CircleDrawOptions}.
     */
    Screen drawCircle(Offset offset, int radius, CircleDrawOptions options);

    /**
     * Draws a {@link Sprite} on the {@link Screen} using the given origin and {@link SpriteDrawOptions}.
     *
     * @see #drawSprite(Sprite, Offset, SpriteDrawOptions)
     */
    Screen drawSprite(Supplier<Sprite> sprite, Offset origin, SpriteDrawOptions options);

    /**
     * Draws a {@link Sprite} on the {@link Screen} using the given origin and {@link SpriteDrawOptions}.
     *
     * @see #drawSprite(Supplier, Offset, SpriteDrawOptions)
     */
    Screen drawSprite(Sprite sprite, Offset origin, SpriteDrawOptions options);

    /**
     * Draws text on the {@link Screen} using {@link SystemTextDrawOptions}. Be warned: The used fonts are system specific and
     * drawing text is kind of slow.
     */
    Screen drawText(Offset offset, String text, SystemTextDrawOptions options);

    /**
     * Draws a sprite based text ({@link Pixelfont}) on the {@link Screen} using the given {@link TextDrawOptions}.
     */
    Screen drawText(Offset offset, String text, TextDrawOptions options);

    /**
     * Fills the {@link Screen} with a repeated {@link Sprite} using the given {@link SpriteFillOptions}.
     */
    Screen fillWith(Sprite sprite, SpriteFillOptions options);

    /**
     * Returns the current {@link ScreenBounds}.
     */
    ScreenBounds bounds();

    /**
     * Draws multiple sorted {@link Sprite sprites} at once. May have slightly better performance than drawing them
     * one by one.
     *
     * @since 1.11.0
     */
    void drawSpriteBatch(SpriteBatch spriteBatch);

    /**
     * Returns the last taken screenshot. Will be empty if no screenshot has been taken.
     */
    Optional<Sprite> lastScreenshot();
}

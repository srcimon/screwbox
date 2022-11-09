package de.suzufa.screwbox.core.graphics;

import java.util.concurrent.Future;

import de.suzufa.screwbox.core.Angle;
import de.suzufa.screwbox.core.Percent;

public interface Window {

    Window drawColor(Color color);

    Color drawColor();

    Window drawRectangle(WindowBounds bounds, Color color);

    default Window fill() {
        return fillWith(drawColor());
    }

    default Window drawRectangle(final WindowBounds bounds) {
        return drawRectangle(bounds, drawColor());
    }

    default Window drawRectangle(final Offset origin, final Dimension size, final Color color) {
        return drawRectangle(new WindowBounds(origin, size), color);
    }

    default Window drawRectangle(final Offset origin, final Dimension size) {
        return drawRectangle(new WindowBounds(origin, size), drawColor());
    }

    Window drawFadingCircle(Offset offset, int diameter, Color color);

    Window drawCircle(Offset offset, int diameter, Color color);

    default Window drawCircle(final Offset offset, final int diameter) {
        return drawCircle(offset, diameter, drawColor());
    }

    Window drawSprite(Future<Sprite> sprite, Offset origin, double scale, Percent opacity, Angle rotation,
            Flip flip, WindowBounds clipArea);

    default Window drawSprite(Future<Sprite> sprite, Offset origin, double scale, Percent opacity) {
        return drawSprite(sprite, origin, scale, opacity, Angle.none(), Flip.NONE, null);
    }

    default Window drawSprite(Future<Sprite> sprite, Offset origin, double scale, Percent opacity, Angle rotation) {
        return drawSprite(sprite, origin, scale, opacity, rotation, Flip.NONE, null);
    }

    default Window drawSprite(final Future<Sprite> sprite, final Offset origin, final Percent opacity) {
        return drawSprite(sprite, origin, 1, opacity, Angle.none(), Flip.NONE, null);
    }

    default Window drawSprite(final Future<Sprite> sprite, final Offset origin) {
        return drawSprite(sprite, origin, Percent.max());
    }

    Window drawSprite(Sprite sprite, Offset origin, double scale, Percent opacity, Angle rotation,
            Flip flip, WindowBounds clipArea);

    default Window drawSprite(Sprite sprite, Offset origin, double scale, Percent opacity) {
        return drawSprite(sprite, origin, scale, opacity, Angle.none(), Flip.NONE, null);
    }

    default Window drawSprite(Sprite sprite, Offset origin, double scale, Percent opacity, Angle rotation) {
        return drawSprite(sprite, origin, scale, opacity, rotation, Flip.NONE, null);
    }

    default Window drawSprite(final Sprite sprite, final Offset origin, final Percent opacity) {
        return drawSprite(sprite, origin, 1, opacity, Angle.none(), Flip.NONE, null);
    }

    default Window drawSprite(final Sprite sprite, final Offset origin) {
        return drawSprite(sprite, origin, Percent.max());
    }

    Window drawText(Offset offset, String text, Pixelfont font, Percent opacity, double scale);

    default Window drawText(Offset offset, String text, Pixelfont font, Percent opacity) {
        return drawText(offset, text, font, opacity, 1);
    }

    default Window drawText(Offset offset, String text, Pixelfont font) {
        return drawText(offset, text, font, Percent.max());
    }

    default Window drawText(Offset offset, String text, Pixelfont font, double scale) {
        return drawText(offset, text, font, Percent.max(), scale);
    }

    Window drawTextCentered(Offset offset, String text, Pixelfont font, Percent opacity, double scale);

    default Window drawTextCentered(Offset offset, String text, Pixelfont font, Percent opacity) {
        return drawTextCentered(offset, text, font, opacity, 1);
    }

    default Window drawTextCentered(Offset offset, String text, Pixelfont font) {
        return drawTextCentered(offset, text, font, Percent.max());
    }

    default Window drawTextCentered(Offset offset, String text, Pixelfont font, double scale) {
        return drawTextCentered(offset, text, font, Percent.max(), scale);
    }

    Window drawText(Offset offset, String text, Font font, Color color);

    default Window drawText(final Offset offset, final String text, final Font font) {
        return drawText(offset, text, font, drawColor());
    }

    Window drawTextCentered(final Offset position, final String text, final Font font, final Color color);

    default Window drawTextCentered(final Offset offset, final String text, final Font font) {
        return drawTextCentered(offset, text, font, drawColor());
    }

    Window fillWith(Offset offset, Sprite sprite, double scale, Percent opacity);

    default Window fillWith(final Sprite sprite, final double scale) {
        return fillWith(Offset.origin(), sprite, scale, Percent.max());
    }

    default Window fillWith(final Offset offset, final Sprite sprite, final double scale) {
        return fillWith(offset, sprite, scale, Percent.max());
    }

    default Window fillWith(final Offset offset, final Sprite sprite) {
        return fillWith(offset, sprite, 1);
    }

    default Window fillWith(final Sprite sprite) {
        return fillWith(sprite, 1);
    }

    Window drawLine(Offset from, Offset to, Color color);

    default Window drawLine(final Offset from, final Offset to) {
        return drawLine(from, to, drawColor());
    }

    Window fillWith(Color color);

    Sprite takeScreenshot();

    Offset center();

    Dimension size();

    Offset position();

    boolean hasFocus();

    Window open();

    Window close();

    Window setTitle(String title);

    Window moveTo(Offset position);

    /**
     * Updates the mouse cursor of to the given {@link MouseCursor} when game is in
     * fullscreen and window mode.
     * 
     * @see #setFullscreenCursor(MouseCursor)
     * @see #setWindowCursor(MouseCursor)
     */
    default Window setCursor(final MouseCursor cursor) {
        setWindowCursor(cursor);
        setFullscreenCursor(cursor);
        return this;
    }

    /**
     * Updates the mouse cursor of to the given {@link MouseCursor} when game is in
     * window mode.
     * 
     * @see #setCursor(MouseCursor)
     * @see #setFullscreenCursor(MouseCursor)
     */
    Window setWindowCursor(MouseCursor cursor);

    /**
     * Updates the mouse cursor of to the given {@link MouseCursor} when game is in
     * fullscreen mode.
     * 
     * @see #setCursor(MouseCursor)
     * @see #setWindowCursor(MouseCursor)
     */
    Window setFullscreenCursor(MouseCursor cursor);

    /**
     * Updates the mouse cursor with the given {@link Sprite} when game is in
     * fullscreen and window mode. Supports only {@link Sprite}s with one
     * {@link Frame}.
     * 
     * @see #setFullscreenCursor(Sprite)
     * @see #setWindowCursor(Sprite)
     */
    default Window setCursor(final Sprite cursor) {
        return setCursor(cursor.singleFrame());
    }

    /**
     * Updates the mouse cursor with the given {@link Sprite} when game is in
     * fullscreen mode. Supports only {@link Sprite}s with one {@link Frame}.
     * 
     * @see #setCursor(Sprite)
     * @see #setWindowCursor(Sprite)
     */
    default Window setFullscreenCursor(Sprite cursor) {
        return setFullscreenCursor(cursor.singleFrame());
    }

    /**
     * Updates the mouse cursor with the given {@link Sprite} when game is in
     * fullscreen mode. Supports only {@link Sprite}s with one {@link Frame}.
     * 
     * @see #setCursor(Sprite)
     * @see #setWindowCursor(Sprite)
     */
    default Window setWindowCursor(Sprite cursor) {
        return setWindowCursor(cursor.singleFrame());
    }

    /**
     * Updates the mouse cursor with the given {@link Frame} when game is in
     * fullscreen and window mode.
     * 
     * @see #setWindowCursor(Frame)
     * @see #setFullscreenCursor(Frame)
     */
    default Window setCursor(final Frame cursor) {
        setWindowCursor(cursor);
        setFullscreenCursor(cursor);
        return this;
    }

    /**
     * Updates the mouse cursor with the given {@link Frame} when game is in
     * fullscreen mode.
     * 
     * @see #setCursor(Frame)
     * @see #setWindowCursor(Frame)
     */
    Window setFullscreenCursor(Frame cursor);

    /**
     * Updates the mouse cursor with the given {@link Frame} when game is in window
     * mode.
     * 
     * @see #setCursor(Frame)
     * @see #setFullscreenCursor(Frame)
     */
    Window setWindowCursor(Frame cursor);

    // TODO: javadoc
    boolean isVisible(WindowBounds bounds);

    // TODO: javadoc
    boolean isVisible(Offset offset);

    /**
     * Returns the title of the {@link Window}.
     * 
     * @see #title()
     * @return
     */
    String title();

}

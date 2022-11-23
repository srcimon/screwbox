package de.suzufa.screwbox.core.graphics;

public interface Window {

    Offset center();

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

    /**
     * Returns the title of the {@link Window}.
     * 
     * @see #title()
     * @return
     */
    String title();

}

package io.github.srcimon.screwbox.core.window;

import io.github.srcimon.screwbox.core.graphics.Frame;
import io.github.srcimon.screwbox.core.graphics.MouseCursor;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Sprite;

import java.util.Optional;

/**
 * Used to control the game window and retrieve information about the game window.
 */
public interface Window {

    /**
     * Returns the current position of the {@link Window}.
     *
     * @see #moveTo(Offset)
     */
    Offset position();

    /**
     * Returns true if the {@link Window} currently has focus.
     */
    boolean hasFocus();

    /**
     * Opens the {@link Window}. Does nothing when {@link Window} is already open.
     *
     * @see #isOpen()
     * @see #close()
     */
    Window open();

    /**
     * Closes the {@link Window}. Does nothing when {@link Window} is already closed.
     *
     * @see #isOpen()
     * @see #open()
     */
    Window close();

    /**
     * Sets the title of the {@link Window}.
     *
     * @see #title()
     */
    Window setTitle(String title);

    /**
     * Moves the {@link Window} to a new position.
     *
     * @see #position()
     */
    Window moveTo(Offset position);

    /**
     * Returns all {@link java.io.File}s droped on the {@link Window} at the current frame.
     */
    Optional<FilesDropedOnWindow> filesDropedOnWindow();

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
     * Sets the application icon. Does not support animated {@link Sprite}s.
     */
    Window setApplicationIcon(Sprite icon);

    /**
     * Returns the title of the {@link Window}.
     *
     * @see #setTitle(String) ()
     */
    String title();

    /**
     * Returns the current size of the {@link Window}.
     */
    Size size();

    /**
     * Returns {@code true} if the {@link Window} is currently open.
     * @see #isClosed()
     */
    boolean isOpen();

    /**
     * Returns {@code false} if the {@link Window} is currently open.
     *
     * @see #isOpen()
     */
    default boolean isClosed() {
        return !isOpen();
    }
}

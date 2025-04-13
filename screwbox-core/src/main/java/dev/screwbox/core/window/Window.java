package dev.screwbox.core.window;

import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sizeable;
import dev.screwbox.core.graphics.Sprite;

import java.io.File;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Used to control and retrieve information about the game window.
 *
 * @see <a href="http://screwbox.dev/docs/core-modules/window">Documentation</a>
 */
public interface Window extends Sizeable {

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
     * <p>
     * Warning: Changing the window title may result in a short flicker of the mouse cursor.
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
     * Returns all {@link File files} dropped on the {@link Window} at the current frame.
     */
    Optional<FilesDroppedOnWindow> filesDroppedOnWindow();

    /**
     * Updates the mouse cursor of to the given {@link MouseCursor} when game is in
     * fullscreen and window mode.
     *
     * @see #setFullscreenCursor(MouseCursor)
     * @see #setWindowCursor(MouseCursor)
     */
    Window setCursor(final MouseCursor cursor);

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
    Window setCursor(final Sprite cursor);

    /**
     * Updates the mouse cursor with the given {@link Sprite} when game is in
     * fullscreen and window mode. Supports only {@link Sprite}s with one
     * {@link Frame}.
     *
     * @see #setFullscreenCursor(Sprite)
     * @see #setWindowCursor(Sprite)
     */
    default Window setCursor(final Supplier<Sprite> cursor) {
        return setCursor(cursor.get());
    }

    /**
     * Updates the mouse cursor with the given {@link Sprite} when game is in
     * fullscreen mode. Supports only {@link Sprite}s with one {@link Frame}.
     *
     * @see #setCursor(Sprite)
     * @see #setWindowCursor(Sprite)
     */
    Window setFullscreenCursor(final Sprite cursor);

    /**
     * Updates the mouse cursor with the given {@link Sprite} when game is in
     * fullscreen mode. Supports only {@link Sprite}s with one {@link Frame}.
     *
     * @see #setCursor(Sprite)
     * @see #setWindowCursor(Sprite)
     */
    Window setWindowCursor(final Sprite cursor);

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
     *
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

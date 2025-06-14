package dev.screwbox.core.mouse;

import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Screen;
import dev.screwbox.core.graphics.Viewport;
import dev.screwbox.core.graphics.World;

/**
 * Subsystem for getting information on the mouse.
 *
 * @see <a href="http://screwbox.dev/docs/core-modules/mouse">Documentation</a>
 */
public interface Mouse {

    /**
     * Returns the current mouse {@link Offset} at the {@link Screen}.
     */
    Offset offset();

    /**
     * Returns the current mouse position in the {@link World}.
     */
    Vector position();

    /**
     * Checks if the Cursor is on the {@link Screen} or not.
     */
    boolean isCursorOnScreen();

    /**
     * Checks if the specified {@link MouseButton} is currently down.
     */
    boolean isDown(MouseButton button);

    /**
     * Checks if the left {@link MouseButton} is currently down.
     *
     * @see #isDown(MouseButton)
     * @see #isDownRight()
     * @since 3.5.0
     */
    default boolean isDownLeft() {
        return isDown(MouseButton.LEFT);
    }

    /**
     * Checks if the right {@link MouseButton} is currently down.
     *
     * @see #isDownLeft()
     * @see #isDown(MouseButton)
     * @since 3.5.0
     */
    default boolean isDownRight() {
        return isDown(MouseButton.RIGHT);
    }

    /**
     * Checks if the given {@link MouseButton} was just pressed (Will be false in
     * the next frame.).
     *
     * @see #isPressedLeft()
     * @see #isPressedRight()
     */
    boolean isPressed(MouseButton button);

    /**
     * Checks if the left mouse button was just pressed (Will be false in the next
     * frame.).
     *
     * @see #isPressed(MouseButton)
     * @see #isPressedLeft()
     */
    default boolean isPressedLeft() {
        return isPressed(MouseButton.LEFT);
    }

    /**
     * Checks if the right mouse button was just pressed (Will be false in the next
     * frame.).
     *
     * @see #isPressedLeft()
     * @see #isPressed(MouseButton)
     */
    default boolean isPressedRight() {
        return isPressed(MouseButton.RIGHT);
    }

    /**
     * Returns the drag movement of the mouse since the last frame.
     */
    Vector drag();

    /**
     * Returns the count of units scrolled since the last frame. Negative values for
     * scrolling down.
     */
    int unitsScrolled();

    /**
     * Returns true if there was any scrolling since the last frame.
     */
    boolean hasScrolled();

    /**
     * Returns true if there was {@link MouseButton} down.
     */
    boolean isAnyButtonDown();

    /**
     * The {@link Viewport} the mouse is currently hovering over.
     *
     * @since 2.5.0
     */
    Viewport hoverViewport();
}

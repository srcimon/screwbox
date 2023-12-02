package io.github.srcimon.screwbox.core.mouse;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.World;

/**
 * Subsystem for getting Information on the Mouse.
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
     * Checks if the given {@link MouseButton} is pressed.
     */
    boolean isDown(MouseButton button);

    /**
     * Checks if the given {@link MouseButton} was just pressed (Will be false in
     * the next frame.).
     * 
     * @see #clickedLeft()
     * @see #clickedRight()
     */
    boolean clicked(MouseButton button);

    /**
     * Checks if the left mouse button was just pressed (Will be false in the next
     * frame.).
     * 
     * @see #clicked(MouseButton)
     * @see #clickedRight()
     */
    default boolean clickedLeft() {
        return clicked(MouseButton.LEFT);
    }

    /**
     * Checks if the right mouse button was just pressed (Will be false in the next
     * frame.).
     * 
     * @see #clickedLeft()
     * @see #clicked(MouseButton)
     */
    default boolean clickedRight() {
        return clicked(MouseButton.RIGHT);
    }

    /**
     * Returns the drag movement of the mouse since the last frame.
     */
    Vector drag();

    /**
     * Returns the count of units scolled since the last frame. Negative values for
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
}

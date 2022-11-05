package de.suzufa.screwbox.core.mouse;

import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Window;
import de.suzufa.screwbox.core.graphics.World;

/**
 * Subsystem for getting Information on the Mouse.
 */
public interface Mouse {

    /**
     * Returns the current mouse-position
     */
    Offset position();

    /**
     * Returns the current mouse-position in the {@link World}.
     */
    Vector worldPosition();

    /**
     * Checks if the Cursor is on the {@link Window} or not.
     */
    boolean isCursorOnWindow();

    /**
     * Checks if the given {@link MouseButton} is pressed.
     */
    boolean isDown(MouseButton button);

    /**
     * Checks if the given {@link MouseButton} was just pressed (Will be false in
     * the next frame.).
     * 
     * @see #justClickedLeft()
     * @see #justClickedRight()
     */
    boolean justClicked(MouseButton button);

    /**
     * Checks if the left mouse button was just pressed (Will be false in the next
     * frame.).
     * 
     * @see #justClicked(MouseButton)
     * @see #justClickedRight()
     */
    default boolean justClickedLeft() {
        return justClicked(MouseButton.LEFT);
    }

    /**
     * Checks if the right mouse button was just pressed (Will be false in the next
     * frame.).
     * 
     * @see #justClickedLeft()
     * @see #justClicked(MouseButton)
     */
    default boolean justClickedRight() {
        return justClicked(MouseButton.RIGHT);
    }

    /**
     * Returns the drag movement of the mouse since the last frame.
     */
    public Vector drag();

    /**
     * Returns the count of units scolled since the last frame. Negative values for
     * scrolling down.
     */
    public int unitsScrolled();

    /**
     * Returns true if there was any scrolling since the last frame.
     */
    public boolean hasScrolled();

    /**
     * Returns true if there was {@link MouseButton} down.
     */
    boolean isAnyButtonDown();
}

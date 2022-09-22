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
     */
    boolean justClicked(MouseButton button);

    // TODO: test and javadoc
    public Vector drag();

    // TODO: test and javadoc
    public int unitsScrolled();

    // TODO: test and javadoc
    public boolean hasScrolled();
}

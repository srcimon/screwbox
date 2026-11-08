package dev.screwbox.core.environment.controls;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.keyboard.DefaultControlSet;

import java.io.Serial;

/**
 * Let the {@link Entity} move on key press.
 *
 * @since 2.15.0
 */
public class MovementControlComponent implements Component {
    //TODO document changes
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Restricts horizontal movement (left and right) when set to {@code true}.
     *
     * @since 3.40.0
     */
    public boolean isLimitedToHorizontalMovement = true;

    /**
     * Enable or disable left right movement control.
     */
    public boolean isEnabled = true;

    /**
     * Acceleration applied when pressing the key for either direction.
     */
    public double acceleration = 600;

    /**
     * Maximum speed that can be reached.
     */
    public double maxSpeed = 100;

    /**
     * Alias for key to press to move to the left.
     */
    public Enum<?> leftAlias = DefaultControlSet.LEFT;

    /**
     * Alias for key to press to move to the right.
     */
    public Enum<?> rightAlias = DefaultControlSet.RIGHT;

    /**
     * Alias for key to press to move upwards.
     *
     * @since 3.40.0
     */
    public Enum<?> upAlias = DefaultControlSet.UP;

    /**
     * Alias for key to press to move downwards.
     *
     * @since 3.40.0
     */
    public Enum<?> downAlias = DefaultControlSet.DOWN;

}

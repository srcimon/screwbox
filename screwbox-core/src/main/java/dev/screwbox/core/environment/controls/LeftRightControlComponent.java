package dev.screwbox.core.environment.controls;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.keyboard.DefaultControlSet;

import java.io.Serial;

/**
 * Let the {@link Entity} move left and right on key press.
 *
 * @since 2.15.0
 */
public class LeftRightControlComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance using the {@link DefaultControlSet}.
     */
    public LeftRightControlComponent() {
        this(DefaultControlSet.LEFT, DefaultControlSet.RIGHT);
    }

    /**
     * Creates a new instance setting the aliases for left and right movement.
     */
    public LeftRightControlComponent(final Enum<?> leftAlias, final Enum<?> rightAlias) {
        this.leftAlias = leftAlias;
        this.rightAlias = rightAlias;
    }

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
    public Enum<?> leftAlias;

    /**
     * Alias for key to press to move to the right.
     */
    public Enum<?> rightAlias;

}

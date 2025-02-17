package io.github.srcimon.screwbox.core.environment.controls;

import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.environment.Entity;

import java.io.Serial;

/**
 * Let the {@link Entity} move left and right on key press.
 */
public class LeftRightControlComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    //TODO FINISH javadoc below
    public LeftRightControlComponent(final Enum<?> left, final Enum<?> right) {
        this.left = left;
        this.right = right;
    }

    public boolean isEnabled = true;
    public double acceleration;
    public double maxSpeed;
    public Enum<?> left;
    public Enum<?> right;

}

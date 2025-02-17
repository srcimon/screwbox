package io.github.srcimon.screwbox.core.environment.controls;

import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.keyboard.DefaultControlSet;

import java.io.Serial;

/**
 * Let the {@link Entity} do a jump on key press. Needs {@link PhysicsComponent} to work.
 * Add {@link SuspendJumpControlComponent} to prevent jumping when not on the ground.
 *
 * @see SuspendJumpControlComponent
 * @since 2.15.0
 */
public class JumpControlComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Alias for the key used to trigger the jump.
     */
    public Enum<?> keyAlias;

    /**
     * The acceleration applied on jumping.
     */
    public double acceleration = 100;

    /**
     * Enable or disable jumping control.
     */
    public boolean isEnabled = true;

    /**
     * Will be automatically updated with last time jumped.
     */
    public Time lastActivation = Time.unset();

    /**
     * Creates a new instance using the {@link DefaultControlSet}.
     */
    public JumpControlComponent() {
        this(DefaultControlSet.JUMP);
    }

    /**
     * Creates a new instance specifying the alias for triggering the jump.
     */
    public JumpControlComponent(final Enum<?> keyAlias) {
        this.keyAlias = keyAlias;
    }
}
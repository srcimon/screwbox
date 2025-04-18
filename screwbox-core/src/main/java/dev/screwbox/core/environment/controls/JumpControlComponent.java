package dev.screwbox.core.environment.controls;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Time;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.keyboard.DefaultControlSet;

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
    public double acceleration = 200;

    /**
     * Enable or disable jumping control.
     */
    public boolean isEnabled = true;

    /**
     * Last time a jump was requested and not yet applied.
     *
     * @see #gracePeriod
     */
    public Time lastUnansweredRequest = Time.unset();

    /**
     * When pressing a jump key within this period before the control is enabled a jump will be triggered.
     * This makes jumping a lot smoother when pressing the key before hitting the ground when combining
     * with {@link SuspendJumpControlComponent}.
     */
    public Duration gracePeriod = Duration.ofMillis(100);

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
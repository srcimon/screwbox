package dev.screwbox.core.environment.smoke;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Duration;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.smoke.Smoke;
import dev.screwbox.core.utils.Noise;

import java.io.Serial;

/**
 * Constantly emits smoke from the entity.
 *
 * @see Smoke
 * @since 3.33.0
 */
public class SmokeEmitterComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Amount of smoke emitted per time.
     */
    public double amount;

    /**
     * Color of emitted smoke.
     */
    public Color color;

    /**
     * Velocity of emit.
     */
    public Vector velocity;

    /**
     * Specify the noise applied on velocity when {@link #velocityTurbulenceRange} has a value.
     *
     * @since 3.39.0
     */
    public Noise velocityTurbulence = Noise.variableInterval(Duration.ofMillis(500));

    /**
     * Specify the variance applied on the velocity of the emitted smoke.
     *
     * @since 3.39.0
     */
    public Angle velocityTurbulenceRange = Angle.none();

    /**
     * Specify if smoke is emitted from whole {@link Entity#bounds()} (default {@code false}) or only from {@link Entity#position()}.
     *
     * @since 3.39.0
     */
    public boolean isPointEmitter = false;

    /**
     * Creates a new instance with default settings.
     */
    public SmokeEmitterComponent() {
        this(1, Color.WHITE, Vector.zero());
    }

    /**
     * Creates a new instance with specified settings.
     */
    public SmokeEmitterComponent(final double amount, final Color color, final Vector velocity) {
        this.amount = amount;
        this.color = color;
        this.velocity = velocity;
    }
}

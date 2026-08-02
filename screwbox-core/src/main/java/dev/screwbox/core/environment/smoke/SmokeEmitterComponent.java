package dev.screwbox.core.environment.smoke;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.smoke.Smoke;

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

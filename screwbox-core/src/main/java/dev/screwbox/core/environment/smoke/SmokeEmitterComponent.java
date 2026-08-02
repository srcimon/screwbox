package dev.screwbox.core.environment.smoke;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.smoke.Smoke;

import java.io.Serial;

//TODO document test

/**
 * Constantly emits smoke from the entity.
 *
 * @see Smoke
 * @since 3.33.0
 */
public class SmokeEmitterComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double amount;
    public Color color;
    public Vector velocity = Vector.zero();

    public SmokeEmitterComponent() {
        this(0, Color.WHITE);
    }

    public SmokeEmitterComponent(double amount, Color color) {
        this.amount = amount;
        this.color = color;
    }
}

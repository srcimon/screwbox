package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.environment.Component;

import java.io.Serial;

/**
 * Applies pressure to soft body. Soft body will expand or retract according to specified pressure value.
 * Adding pressure to a soft body which tries to preserve shape using {@link SoftBodyShapeComponent} might also gain
 * unwanted motion.
 *
 * @since 3.18.0
 */
public class SoftBodyPressureComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Pressure applied on soft body. Negative numbers might cause a mess when value is too low to deflate body completely.
     */
    public double pressure;

    /**
     * Creates a new instance using no pressure.
     *
     * @since 3.19.0
     */
    public SoftBodyPressureComponent() {
        this(0);
    }

    /**
     * Creates a new instance using specified pressure.
     */
    public SoftBodyPressureComponent(final double pressure) {
        this.pressure = pressure;
    }
}

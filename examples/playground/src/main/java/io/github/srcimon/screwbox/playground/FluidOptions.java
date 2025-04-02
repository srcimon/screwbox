package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.utils.Validate;

import java.io.Serial;
import java.io.Serializable;

/**
 * Options to configure specific behaviour of a {@link Fluid}.
 *
 * @param nodeCount    number of surface nodes simulated
 * @param dampening    reduction of wave speed over time
 * @param retract      speed used to return to normal position
 * @param transmission amount of wave height used to affect neighbour surface nodes
 * @since 2.19.0
 */
public record FluidOptions(int nodeCount, double dampening, double retract,
                           double transmission) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public FluidOptions {
        Validate.positive(nodeCount, "node count must be positive");
        Validate.positive(dampening, "dampening must be positive");
        Validate.positive(retract, "retract must be positive");
        Validate.positive(transmission, "transmission must be positive");
    }

    /**
     * Create a new instance using the specified node count.
     */
    public static FluidOptions nodeCount(final int nodeCount) {
        return new FluidOptions(nodeCount, 1.5, 25, 30);
    }

    /**
     * Create a new instance using specific {@link #dampening()} value.
     */
    public FluidOptions dampening(final double dampening) {
        return new FluidOptions(nodeCount, dampening, retract, transmission);
    }

    /**
     * Create a new instance using specific {@link #retract(double)} value.
     */
    public FluidOptions retract(final double retract) {
        return new FluidOptions(nodeCount, dampening, retract, transmission);
    }

    /**
     * Create a new instance using specific {@link #transmission()} value.
     */
    public FluidOptions transmission(final double transmission) {
        return new FluidOptions(nodeCount, dampening, retract, transmission);
    }
}

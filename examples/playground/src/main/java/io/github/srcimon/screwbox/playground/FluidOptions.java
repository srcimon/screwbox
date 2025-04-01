package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.utils.Validate;

import java.io.Serial;
import java.io.Serializable;

public record FluidOptions(int nodeCount, double dampening, double retract, double transmission) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public FluidOptions {
        Validate.positive(nodeCount, "node count must be positive");
        Validate.positive(dampening, "dampening must be positive");
        Validate.positive(retract, "retract must be positive");
        Validate.positive(transmission, "transmission must be positive");
    }

    public static FluidOptions nodeCount(int nodeCount) {
        return new FluidOptions(nodeCount, 1.5, 25, 30);
    }

    public FluidOptions dampening(double dampening) {
        return new FluidOptions(nodeCount, dampening, retract, transmission);
    }

    public FluidOptions retract(double retract) {
        return new FluidOptions(nodeCount, dampening, retract, transmission);
    }

    public FluidOptions transmission(double transmission) {
        return new FluidOptions(nodeCount, dampening, retract, transmission);
    }
}

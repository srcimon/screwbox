package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

/**
 * Creates a fluid that can be used to create animated fluid visuals and interact with physics entities.
 *
 * @see FluidSystem
 * @since 2.19.0
 */
public class FluidComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final double[] height;
    public final double[] speed;

    /**
     * Number of wave nodes.
     */
    public final int nodeCount;

    /**
     * Speed used to return to normal position
     */
    public double retract = 25;

    /**
     * Reduction of wave speed over time.
     */
    public double dampening = 1.5;

    /**
     * Amount of wave height used to affect neighbour surface nodes.
     */
    public double transmission = 30;

    /**
     * Creates a new instance using the specified node count.
     */
    public FluidComponent(final int nodeCount) {
        height = new double[nodeCount];
        speed = new double[nodeCount];
        this.nodeCount = nodeCount;
    }

}

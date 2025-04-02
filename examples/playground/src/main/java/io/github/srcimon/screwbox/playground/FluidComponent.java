package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

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

    public FluidComponent(final int nodeCount) {
        height = new double[nodeCount];
        speed = new double[nodeCount];
        this.nodeCount = nodeCount;
    }

}

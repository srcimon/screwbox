package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.loop.Loop;

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
     * Maximum {@link Loop#delta()} used for calculation to avoid escalating wave heights on low frame rates.
     */
    public double maxDelta = 0.01;

    /**
     * Creates a new instance using the specified node count.
     */
    public FluidComponent(final int nodeCount) {
        height = new double[nodeCount];
        speed = new double[nodeCount];
        this.nodeCount = nodeCount;
    }

}

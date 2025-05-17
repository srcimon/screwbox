package dev.screwbox.core.environment.fluids;

import dev.screwbox.core.Path;
import dev.screwbox.core.environment.Component;

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

    /**
     * Height of each node above or below normal. Not meant for customizing.
     */
    public final double[] height;

    /**
     * Vertical speed of each node. Not meant for customizing.
     */
    public final double[] speed;

    /**
     * Surface of the fluid within the game world. Will be updated automatically. Not meant for customizing.
     *
     * @since 2.20.0
     */
    public Path surface;

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

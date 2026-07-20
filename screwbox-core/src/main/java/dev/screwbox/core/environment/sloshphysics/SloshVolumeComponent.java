package dev.screwbox.core.environment.sloshphysics;

import dev.screwbox.core.Polygon;
import dev.screwbox.core.environment.Component;

import java.io.Serial;

/**
 * Creates a liquid that can be used to create animated liquid visuals and interact with physics entities.
 *
 * @see <a href="https://screwbox.dev/docs/guides/slosh-physics/">Guide: Slosh phyics</a>
 * @see SloshVolumeSystem
 * @since 2.19.0
 */
public class SloshVolumeComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Height of each node above or below normal. Not intended for customizing.
     */
    public final double[] height;

    /**
     * Vertical speed of each node. Not intended for customizing.
     */
    public final double[] speed;

    /**
     * Surface of the fluid within the game world. Will be updated automatically. Not intended for customizing.
     *
     * @since 2.20.0
     */
    public Polygon surface;

    /**
     * Outline of the fluid within the game world. Will be updated automatically. Not intended for customizing.
     *
     * @since 3.28.0
     */
    public Polygon outline;

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
    public SloshVolumeComponent(final int nodeCount) {
        height = new double[nodeCount];
        speed = new double[nodeCount];
        this.nodeCount = nodeCount;
    }

}

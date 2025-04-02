package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;

import java.io.Serial;
import java.io.Serializable;

//TODO convert to simple component
public class Fluid implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final FluidOptions options;

    public double getHeight(int nodeNr) {
        return height[nodeNr];
    }

    public int nodeCount() {
        return nodeCount;
    }

    public final double[] height;
    public final double[] speed;
    public final int nodeCount;

    public Fluid(final FluidOptions options) {
        this.options = options;
        height = new double[options.nodeCount()];
        speed = new double[options.nodeCount()];
        nodeCount = options.nodeCount();
    }

    public void update(final double delta) {
        for (int i = 0; i < nodeCount; i++) {
            final double deltaLeft = i > 0 ? height[i] - height[i - 1] : 0;
            final double deltaRight = i < nodeCount - 1 ? height[i] - height[i + 1] : 0;
            height[i] += +delta * speed[i];

            final double sidePull = deltaLeft * delta * options.transmission() + deltaRight * delta * options.transmission();
            final double retract = height[i] * options.retract() * delta;
            final double dampen = options.dampening() * speed[i] * delta;
            speed[i] += -sidePull - retract - dampen;
        }

    }

    public void interact(final Bounds projection, final Bounds interaction, final double strength) {
        var nodePositions = FluidSupport.calculateSurface(projection, this);

        for (int i = 0; i < nodeCount; i++) {
            final Vector nodePosition = nodePositions.get(i);
            if (interaction.contains(nodePosition)) {
                speed[i] += strength;
            }
        }
    }

}

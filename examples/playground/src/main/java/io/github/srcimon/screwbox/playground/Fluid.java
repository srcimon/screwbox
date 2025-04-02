package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;

import java.io.Serial;
import java.io.Serializable;

//TODO convert to simple component
public class Fluid implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public final double[] height;
    public final double[] speed;
    public final int nodeCount;
    public double retract = 25;
    public double dampening = 1.5;
    public double transmission = 30;

    //     * @param nodeCount    number of surface nodes simulated
// * @param dampening    reduction of wave speed over time
// * @param retract      speed used to return to normal position
// * @param transmission amount of wave height used to affect neighbour surface nodes
//
    public Fluid(final int nodeCount) {
        height = new double[nodeCount];
        speed = new double[nodeCount];
        this.nodeCount = nodeCount;
    }

    public void update(final double delta) {
        for (int i = 0; i < nodeCount; i++) {
            final double deltaLeft = i > 0 ? height[i] - height[i - 1] : 0;
            final double deltaRight = i < nodeCount - 1 ? height[i] - height[i + 1] : 0;
            height[i] += delta * speed[i];

            final double sidePull = deltaLeft * delta * transmission + deltaRight * delta * transmission;
            final double retraction = height[i] * retract  * delta;
            final double dampen = dampening * speed[i] * delta;
            speed[i] += -sidePull - retraction - dampen;
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

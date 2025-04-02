package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

//TODO convert to simple component
public class FluidComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final double[] height;
    public final double[] speed;
    public final int nodeCount;
    public double retract = 25;
    public double dampening = 1.5;
    public double transmission = 30;

    //TODO Document
    //     * @param nodeCount    number of surface nodes simulated
// * @param dampening    reduction of wave speed over time
// * @param retract      speed used to return to normal position
// * @param transmission amount of wave height used to affect neighbour surface nodes
//
    public FluidComponent(final int nodeCount) {
        height = new double[nodeCount];
        speed = new double[nodeCount];
        this.nodeCount = nodeCount;
    }

}

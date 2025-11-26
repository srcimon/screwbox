package dev.screwbox.playground.flexphysics;

import dev.screwbox.core.environment.Component;

//TODO environment.enableFlexPhysics()
public class FlexStructureComponent implements Component {

    public final int[] targetIds;
    public final double[] lengths;
    public double retract = 20;
    public double expand = 20;
    public double flexibility = 20;

    public FlexStructureComponent(final int... targetIds) {
        this.targetIds = targetIds;
        this.lengths = new double[targetIds.length];
    }
}

package dev.screwbox.playground.joints;

import dev.screwbox.core.environment.Component;

//TODO environment.enableJoints()
public class JointStructureComponent implements Component {

    public final int[] targetIds;
    public final double[] lengths;
    public double retract = 20;
    public double expand = 20;
    public double flexibility = 20;

    public JointStructureComponent(final int... targetIds) {
        this.targetIds = targetIds;
        this.lengths = new double[targetIds.length];
    }
}

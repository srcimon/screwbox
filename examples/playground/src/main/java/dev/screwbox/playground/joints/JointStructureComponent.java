package dev.screwbox.playground.joints;

import dev.screwbox.core.Angle;
import dev.screwbox.core.environment.Component;

import java.util.List;

//TODO environment.enableJoints()
public class JointStructureComponent implements Component {

    public final int[] targetEntityIds;
    public final double[] lengths;
    public double retract = 20;
    public double expand = 20;
    public double stiffness = 20;

    public JointStructureComponent(int... targetEntityIds) {
        this.targetEntityIds = targetEntityIds;
        this.lengths = new double[targetEntityIds.length];
    }
}

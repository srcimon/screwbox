package dev.screwbox.playground.joint;

import dev.screwbox.core.environment.Component;

import java.util.ArrayList;
import java.util.List;

public class JointComponent implements Component {

    public Joint joint;
    public List<Joint> additionalJoints = new ArrayList<>();

    public JointComponent(final Joint joint) {
        this.joint = joint;
    }

    public JointComponent(final Joint... joints) {
        this.joint = joints[0];//TODO Bug!
        for(int i = 1; i < joints.length; i++) {
            additionalJoints.add(joints[i]);
        }
    }
}

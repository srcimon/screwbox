package dev.screwbox.playground.joint;

import dev.screwbox.core.environment.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//TODO JointComponent
//TODO MultiJointComponent!!!
public class JointComponent implements Component {

    public Joint joint;
    public List<Joint> additionalJoints = new ArrayList<>();

    public JointComponent(final Joint joint) {
        this.joint = joint;
    }

    public JointComponent(final Joint... joints) {
        this.joint = joints[0];//TODO Bug!
        additionalJoints.addAll(Arrays.asList(joints).subList(1, joints.length));
    }
}

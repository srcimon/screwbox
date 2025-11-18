package dev.screwbox.playground.joint;

import dev.screwbox.core.environment.Component;

import java.util.List;

public class JointComponent implements Component {

    public List<Joint> joints;
//TODO PrimaryJoint
    //TODO AdditionalJoints
    public JointComponent(List<Joint> joints) {
        this.joints = joints;
    }
}

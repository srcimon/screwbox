package dev.screwbox.playground.joint;

import dev.screwbox.core.environment.Component;

//TODO JointComponent
//TODO MultiJointComponent!!!
public class SoftJointComponent implements Component {

    public Joint joint;

    public SoftJointComponent(final Joint joint) {
        this.joint = joint;
    }
}

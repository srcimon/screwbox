package dev.screwbox.playground.joint;

import dev.screwbox.core.environment.Component;

//TODO JointComponent
//TODO MultiJointComponent!!!
public class LinkJointComponent implements Component {

    public Joint joint;

    public LinkJointComponent(final Joint joint) {
        this.joint = joint;
    }
}

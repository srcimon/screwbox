package dev.screwbox.playground.flex;

import dev.screwbox.core.environment.Component;

//TODO JointComponent
//TODO MultiJointComponent!!!
public class FlexLinkComponent implements Component {

    public Joint joint;

    public FlexLinkComponent(final Joint joint) {
        this.joint = joint;
    }
}

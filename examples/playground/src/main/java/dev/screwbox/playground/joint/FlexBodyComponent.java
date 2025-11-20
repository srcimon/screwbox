package dev.screwbox.playground.joint;

import dev.screwbox.core.environment.Component;

//TODO JointComponent
//TODO MultiJointComponent!!!
public class FlexBodyComponent implements Component {

    public Joint joint;

    public FlexBodyComponent(final Joint joint) {
        this.joint = joint;
    }
}

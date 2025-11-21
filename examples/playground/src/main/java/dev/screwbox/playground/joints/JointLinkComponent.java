package dev.screwbox.playground.joints;

import dev.screwbox.core.environment.Component;

public class JointLinkComponent implements Component {

    public Joint joint;

    public JointLinkComponent(final Joint joint) {
        this.joint = joint;
    }
}

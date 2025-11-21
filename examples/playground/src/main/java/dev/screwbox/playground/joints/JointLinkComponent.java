package dev.screwbox.playground.joints;

import dev.screwbox.core.environment.Component;

public class JointLinkComponent implements Component {

    public Joint link;

    public JointLinkComponent(final Joint link) {
        this.link = link;
    }
}

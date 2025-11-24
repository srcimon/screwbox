package dev.screwbox.playground.joints;

import dev.screwbox.core.environment.Component;

public class JointLinkComponent implements Component {

    public final Joint link;//TODO inline Joint inside component

    public JointLinkComponent(final Joint link) {
        this.link = link;
    }
}

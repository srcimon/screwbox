package dev.screwbox.playground.joints;

import dev.screwbox.core.Angle;
import dev.screwbox.core.environment.Component;

public class JointLinkComponent implements Component {

    public final Joint link;
    public Angle angle = Angle.none();

    public JointLinkComponent(final Joint link) {
        this.link = link;
    }
}

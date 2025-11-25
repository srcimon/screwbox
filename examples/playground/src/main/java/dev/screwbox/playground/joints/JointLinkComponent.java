package dev.screwbox.playground.joints;

import dev.screwbox.core.Angle;
import dev.screwbox.core.environment.Component;

public class JointLinkComponent implements Component {

    public JointLinkComponent(final int targetEntityId) {
        this.targetEntityId = targetEntityId;
    }

    public final int targetEntityId;
    public Angle angle = Angle.none();
    public double length;
    public double retract = 20;
    public double expand = 20;
    public double stiffness = 20;
}

package dev.screwbox.playground.flexphysics;

import dev.screwbox.core.Angle;
import dev.screwbox.core.environment.Component;

public class FlexLinkComponent implements Component {

    public FlexLinkComponent(final int targetId) {
        this.targetId = targetId;
    }

    public final int targetId;
    public Angle angle = Angle.none();
    public double length;
    public double retract = 20;
    public double expand = 20;
    public double flexibility = 20;
}

package dev.screwbox.core.environment.flexphysics;

import dev.screwbox.core.Angle;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
//TODO Fill in rope guide documentation
//TODO javadoc for all new components
//TODO Blog on Flex physics

/**
 * Links one {@link Entity} to another with a flexible spring. Used to create ropes and soft bodies.
 *
 * @since 3.16.0
 */
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

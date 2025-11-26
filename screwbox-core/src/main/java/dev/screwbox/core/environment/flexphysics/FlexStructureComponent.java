package dev.screwbox.core.environment.flexphysics;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;

/**
 * Links one {@link Entity} to multiple others with flexible springs. Used to create flexible structures.
 * When intending to link only to one other entity, using {@link FlexLinkComponent} should be preferred.
 *
 * @since 3.16.0
 */
public class FlexStructureComponent implements Component {

    public final int[] targetIds;
    public final double[] lengths;
    public double retract = 20;
    public double expand = 20;
    public double flexibility = 20;

    public FlexStructureComponent(final int... targetIds) {
        this.targetIds = targetIds;
        this.lengths = new double[targetIds.length];
    }
}

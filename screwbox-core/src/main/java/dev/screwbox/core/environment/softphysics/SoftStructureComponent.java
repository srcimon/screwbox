package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;

/**
 * Links one {@link Entity} to multiple others with flexible links. Used to create flexible structures.
 * When intending to link only to one other entity, using {@link SoftLinkComponent} should be preferred.
 *
 * @since 3.16.0
 */
public class SoftStructureComponent implements Component {

    public SoftStructureComponent(final int... targetIds) {
        this.targetIds = targetIds;
        this.lengths = new double[targetIds.length];
    }

    public final int[] targetIds;
    public final double[] lengths;
    public double retract = 20;
    public double expand = 20;
    public double flexibility = 20;
}

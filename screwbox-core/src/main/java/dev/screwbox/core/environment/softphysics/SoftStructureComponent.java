package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.utils.ListUtil;

import java.util.List;

/**
 * Links one {@link Entity} to multiple others with flexible links. Used to create flexible structures.
 * When intending to link only to one other entity, using {@link SoftLinkComponent} should be preferred.
 *
 * @since 3.16.0
 */
public class SoftStructureComponent implements Component {

    /**
     * Creates a new instance with the specified target ids.
     */
    public SoftStructureComponent(final List<Integer> targetIds) {
        this(ListUtil.toIntArray(targetIds));
    }

    /**
     * Creates a new instance with the specified target ids.
     */
    public SoftStructureComponent(final int... targetIds) {
        this.targetIds = targetIds;
        this.lengths = new double[targetIds.length];
    }

    /**
     * Ids of the entities that are linked.
     */
    public final int[] targetIds;

    /**
     * Length of the corresponding link. Will be automatically filled when zero.
     */
    public final double[] lengths;

    /**
     * Retract strength of the links.
     */
    public double retract = 20;

    /**
     * Expand strength of the links.
     */
    public double expand = 20;

    /**
     * Flexibility of the links.
     */
    public double flexibility = 20;
}

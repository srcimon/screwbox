package dev.screwbox.playground.elastics;

import dev.screwbox.core.environment.Component;

//TODO environment.enableJoints()
//TODO Rename?
// enableElastics()
// FlexLinkComponent, FlexStructureComponent?? FlexSystem
// ElasticLinkComponent, ElasticStructureComponent, ElasticSystem
public class ElasticStructureComponent implements Component {

    public final int[] targetIds;
    public final double[] lengths;
    public double retract = 20;
    public double expand = 20;
    public double flexibility = 20;

    public ElasticStructureComponent(final int... targetIds) {
        this.targetIds = targetIds;
        this.lengths = new double[targetIds.length];
    }
}

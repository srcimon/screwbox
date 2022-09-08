package de.suzufa.screwbox.core.entityengine.components;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.entityengine.Component;

public class ReflectionComponent implements Component {

    private static final long serialVersionUID = 1L;

    public final Percentage opacityReduction;

    public ReflectionComponent() {
        this(Percentage.threeQuarters());
    }

    public ReflectionComponent(Percentage opacityReduction) {
        this.opacityReduction = opacityReduction;
    }
}

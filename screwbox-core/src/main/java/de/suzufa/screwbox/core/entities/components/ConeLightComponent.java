package de.suzufa.screwbox.core.entities.components;

import de.suzufa.screwbox.core.Angle;
import de.suzufa.screwbox.core.entities.Component;
import de.suzufa.screwbox.core.graphics.LightOptions;

public class ConeLightComponent implements Component {

    private static final long serialVersionUID = 1L;

    public LightOptions options;
    public Angle direction;
    public Angle cone;

    public ConeLightComponent(Angle direction, Angle cone, LightOptions options) {
        this.direction = direction;
        this.cone = cone;
        this.options = options;
    }
}

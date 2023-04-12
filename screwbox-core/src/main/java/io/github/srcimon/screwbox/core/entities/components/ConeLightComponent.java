package io.github.srcimon.screwbox.core.entities.components;

import io.github.srcimon.screwbox.core.Angle;
import io.github.srcimon.screwbox.core.entities.Component;
import io.github.srcimon.screwbox.core.graphics.LightOptions;

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

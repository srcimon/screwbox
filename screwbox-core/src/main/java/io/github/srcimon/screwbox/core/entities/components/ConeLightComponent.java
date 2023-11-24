package io.github.srcimon.screwbox.core.entities.components;

import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.entities.Component;
import io.github.srcimon.screwbox.core.graphics.LightOptions;

public class ConeLightComponent implements Component {

    private static final long serialVersionUID = 1L;

    public LightOptions options;
    public Rotation direction;
    public Rotation cone;

    public ConeLightComponent(Rotation direction, Rotation cone, LightOptions options) {
        this.direction = direction;
        this.cone = cone;
        this.options = options;
    }
}

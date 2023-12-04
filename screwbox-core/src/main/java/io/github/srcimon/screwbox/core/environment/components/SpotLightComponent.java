package io.github.srcimon.screwbox.core.environment.components;

import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.graphics.LightOptions;

public class SpotLightComponent implements Component {

    private static final long serialVersionUID = 1L;

    public LightOptions options;

    public SpotLightComponent(LightOptions options) {
        this.options = options;
    }
}

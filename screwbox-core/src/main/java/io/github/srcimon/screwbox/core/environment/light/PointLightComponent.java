package io.github.srcimon.screwbox.core.environment.light;

import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.graphics.LightOptions;

import java.io.Serial;

public class PointLightComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public LightOptions options;

    public PointLightComponent(final LightOptions options) {
        this.options = options;
    }
}

package io.github.simonbas.screwbox.core.entities.components;

import io.github.simonbas.screwbox.core.entities.Component;
import io.github.simonbas.screwbox.core.graphics.LightOptions;

public class PointLightComponent implements Component {

    private static final long serialVersionUID = 1L;

    public LightOptions options;

    public PointLightComponent(final LightOptions options) {
        this.options = options;
    }
}

package de.suzufa.screwbox.core.entities.components;

import de.suzufa.screwbox.core.entities.Component;
import de.suzufa.screwbox.core.graphics.LightOptions;

public class PointLightComponent implements Component {

    private static final long serialVersionUID = 1L;

    public LightOptions options;

    public PointLightComponent(final LightOptions options) {
        this.options = options;
    }
}

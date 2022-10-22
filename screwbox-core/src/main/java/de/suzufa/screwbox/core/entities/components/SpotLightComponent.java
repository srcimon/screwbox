package de.suzufa.screwbox.core.entities.components;

import de.suzufa.screwbox.core.entities.Component;
import de.suzufa.screwbox.core.graphics.LightOptions;

public class SpotLightComponent implements Component {

    private static final long serialVersionUID = 1L;

    public LightOptions options;

    public SpotLightComponent(LightOptions options) {
        this.options = options;
    }
}

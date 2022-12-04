package de.suzufa.screwbox.examples.platformer.components;

import de.suzufa.screwbox.core.entities.Component;

public class UseLightComponent implements Component {

    private static final long serialVersionUID = 1L;

    public boolean useLight;

    public UseLightComponent(boolean useLight) {
        this.useLight = useLight;
    }
}

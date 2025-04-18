package dev.screwbox.platformer.components;

import dev.screwbox.core.environment.Component;

import java.io.Serial;

public class UseLightComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public boolean useLight;

    public UseLightComponent(boolean useLight) {
        this.useLight = useLight;
    }
}

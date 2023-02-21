package io.github.simonbas.screwbox.core.graphics;

import java.util.EventListener;

public interface GraphicsConfigurationListener extends EventListener {

    enum ConfigurationProperty {
        RESOLUTION,
        WINDOW_MODE,
        ANTIALIASING,
        LIGHTMAP_BLUR,
        LIGHTMAP_SCALE
    }

    void configurationChanged(ConfigurationProperty changedProperty);
}

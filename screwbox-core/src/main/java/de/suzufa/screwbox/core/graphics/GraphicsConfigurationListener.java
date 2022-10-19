package de.suzufa.screwbox.core.graphics;

import java.util.EventListener;

public interface GraphicsConfigurationListener extends EventListener {

    enum ConfigurationProperty {
        RESOLUTION,
        WINDOW_MODE,
        ANTIALIASING
    }

    void configurationChanged(ConfigurationProperty changedProperty);
}

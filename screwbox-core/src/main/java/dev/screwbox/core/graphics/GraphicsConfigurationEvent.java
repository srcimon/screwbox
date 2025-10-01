package dev.screwbox.core.graphics;

import java.util.EventObject;

/**
 * Occurs on any Change of the {@link GraphicsConfiguration} properties.
 */
public class GraphicsConfigurationEvent extends EventObject {

    private final ConfigurationProperty changedProperty;

    //TODO DOCUMENT ALL ENUM VALUES
    public enum ConfigurationProperty {
        RESOLUTION,
        WINDOW_MODE,
        ANTIALIASING,
        LIGHTMAP_BLUR,
        LIGHT_FALLOFF,
        AUTO_ENABLE_LIGHT,
        LIGHT_ENABLED,
        OVERLAY_SHADER,
        BACKGROUND_COLOR,
        LIGHT_QUALITY,
        LENS_FLARE_ENABLED
    }

    public GraphicsConfigurationEvent(final Object source, final ConfigurationProperty changedProperty) {
        super(source);
        this.changedProperty = changedProperty;
    }

    /**
     * Returns the property of the {@link GraphicsConfiguration} that has been changed.
     */
    public ConfigurationProperty changedProperty() {
        return changedProperty;
    }
}

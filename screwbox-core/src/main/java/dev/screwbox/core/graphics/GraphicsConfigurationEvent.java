package dev.screwbox.core.graphics;

import java.util.EventObject;

/**
 * Occurs on any Change of the {@link GraphicsConfiguration} properties.
 */
public class GraphicsConfigurationEvent extends EventObject {

    private final ConfigurationProperty changedProperty;

    /**
     * Changed configuration property.
     */
    public enum ConfigurationProperty {
        /**
         * {@link GraphicsConfiguration#resolution()} was changed.
         */
        RESOLUTION,

        /**
         * {@link GraphicsConfiguration#isFullscreen()} was changed.
         */
        FULLSCREEN,

        /**
         * {@link GraphicsConfiguration#isUseAntialiasing()} was changed.
         */
        ANTIALIASING,

        /**
         * {@link GraphicsConfiguration#lightmapBlur()} was changed.
         */
        LIGHT_BLUR,

        /**
         * {@link GraphicsConfiguration#lightFalloff()} was changed.
         */
        LIGHT_FALLOFF,

        /**
         * {@link GraphicsConfiguration#isAutoEnableLight()} was changed.
         */
        AUTO_ENABLE_LIGHT,

        /**
         * {@link GraphicsConfiguration#isLightEnabled()} was changed.
         */
        LIGHT_ENABLED,

        /**
         * {@link GraphicsConfiguration#overlayShader()} was changed.
         */
        OVERLAY_SHADER,

        /**
         * {@link GraphicsConfiguration#backgroundColor()} was changed.
         */
        BACKGROUND_COLOR,

        /**
         * {@link GraphicsConfiguration#lightQuality()} was changed.
         */
        LIGHT_QUALITY,

        /**
         * {@link GraphicsConfiguration#isLensFlareEnabled()} was changed.
         */
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

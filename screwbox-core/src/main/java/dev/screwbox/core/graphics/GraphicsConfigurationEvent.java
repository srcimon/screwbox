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
         * {@link GraphicsConfiguration#shockwaveCellLimit()} was changed.
         */
        SHOCKWAVE_CELL_LIMIT,

        /**
         * {@link GraphicsConfiguration#indirectLightIntensity()} was changed.
         */
        INDIRECT_LIGHT_INTENSITY,

        /**
         * {@link GraphicsConfiguration#lightBounceIntensityLoss()}  was changed.
         */
        LIGHT_BOUNCE_INTENSITY_LOSS,

        /**
         * {@link GraphicsConfiguration#lightBounceLengthLoss()}  was changed.
         */
        LIGHT_BOUNCE_LENGTH_LOSS,

        /**
         * {@link GraphicsConfiguration#maxLightBounces()} was changed.
         */
        MAX_LIGHT_BOUNCES,

        /**
         * {@link GraphicsConfiguration#indirectLightDiameter()} was changed.
         */
        INDIRECT_LIGHT_DIAMETER,

        /**
         * {@link GraphicsConfiguration#isLensFlareEnabled()} was changed.
         */
        LENS_FLARE_ENABLED,

        /**
         * {@link GraphicsConfiguration#isSmokeEnabled()} was changed.
         */
        SMOKE_ENABLED,

        /**
         * {@link GraphicsConfiguration#smokeBlur()} was changed.
         */
        SMOKE_BLUR,

        /**
         * {@link GraphicsConfiguration#smokeOpacity()} was changed.
         */
        SMOKE_OPACITY,

        /**
         * {@link GraphicsConfiguration#smokeCellSize()} was changed.
         */
        SMOKE_CELL_SIZE,

        /**
         * {@link GraphicsConfiguration#smokeCellPadding()} was changed.
         */
        SMOKE_CELL_PADDING,

        /**
         * {@link GraphicsConfiguration#isAutoEnableSmoke()} was changed.
         */
        SMOKE_AUTO_ENABLE,

        /**
         * {@link GraphicsConfiguration#smokeScale()} was changed.
         */
        SMOKE_SCALE
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

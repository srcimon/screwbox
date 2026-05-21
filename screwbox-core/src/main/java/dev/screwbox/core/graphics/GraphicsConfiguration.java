package dev.screwbox.core.graphics;

import dev.screwbox.core.Percent;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * Configuration of major {@link Graphics} and rendering properties. Every change creates a
 * {@link GraphicsConfigurationEvent} that can be used to adjust to the new configuration.
 */
public class GraphicsConfiguration {

    /**
     * Resolution used when not changed during lifetime.
     *
     * @since 3.10.0
     */
    public static final Size DEFAULT_RESOLUTION = Size.of(1280, 720);

    private final List<GraphicsConfigurationListener> listeners = new ArrayList<>();
    private Size resolution = DEFAULT_RESOLUTION;
    private boolean isFullscreen = false;
    private boolean useAntialiasing = false;
    private boolean isAutoEnableLight = true;
    private boolean isLightEnabled = false;
    private boolean isLensFlareEnabled = true;
    private int lightBlur = 3;
    private int shockwaveCellLimit = 10_000;
    private Percent lightFalloff = Percent.max();
    private Color backgroundColor = Color.BLACK;
    private ShaderSetup overlayShader = null;
    private Percent lightQuality = Percent.quarter();
    private Percent lightBounceLossFactor = Percent.of(0.1);
    private Percent indirectLightIntensity = Percent.of(0.9);
    private float indirectLightDiameter = 16f;
    private int maxLightBounces = 2;

    //TODO add indirect light to graphics documentation

    //TODO TEST

    //TODO blogpost on indirect light

    /**
     * Returns {@code true} if indirect light is enabled. Is controlled by {@link #maxLightBounces()}
     * (disabled when zero) and {@link #indirectLightIntensity()} (disabled when zero).
     *
     * @since 3.30.0
     */
    public boolean isIndirectLightEnabled() {
        return indirectLightIntensity.hasValue() && maxLightBounces() > 0;
    }

    //TODO test

    /**
     * Sets the diameter of indirect light rays. Illumination will increase significantly with larger diameters.
     * Must be in range 4 to 64. Default value is 16.
     *
     * @since 3.30.0
     */
    public GraphicsConfiguration setIndirectLightDiameter(final float diameter) {
        Validate.range(diameter, 4f, 64f, "diameter must be in range 4 to 64");
        this.indirectLightDiameter = diameter;
        return this;
    }


    //TODO test

    /**
     * Returns the diameter of indirect light rays.
     *
     * @since 3.30.0
     */
    public float indirectLightDiameter() {
        return indirectLightDiameter;
    }

    /**
     * Specify the maximum number of consecutive bounces that light will make when hitting occluders. Indirect light
     * will be disabled if value is set to 0. Default value is 2. Indirect light is very expensive, turning it off increases fps
     * significantly. Can also be turned off by setting {@link #setIndirectLightIntensity(Percent)} to {@link Percent#zero()}
     * to zero.
     *
     * @see #maxLightBounces()
     * @since 3.30.0
     */
    //TODO Test
    public GraphicsConfiguration setMaxLightBounces(final int maxLightBounces) {
        Validate.zeroOrPositive(maxLightBounces, "max light bounces must be positive");
        this.maxLightBounces = maxLightBounces;
        notifyListeners(GraphicsConfigurationEvent.ConfigurationProperty.MAX_LIGHT_BOUNCES);
        return this;
    }

    /**
     * Returns the maximum number of light bounces.
     *
     * @since 3.30.0
     */
    //TODO Test
    public int maxLightBounces() {
        return maxLightBounces;
    }

    /**
     * Sets the loss of light intensity when it bounces of an occluder. Must be below max value. Default is 10%.
     *
     * @since 3.30.0
     */
    public GraphicsConfiguration setLightBounceLossFactor(final Percent lossFactor) {
        Objects.requireNonNull(lossFactor, "loss factor must not be null");
        Validate.isFalse(lossFactor::isMax, "loss factor must below maximum value");
        this.lightBounceLossFactor = lossFactor;
        notifyListeners(GraphicsConfigurationEvent.ConfigurationProperty.LIGHT_BOUNCE_LOSS_FACTOR);
        return this;
    }

    /**
     * Returns the loss of light intensity when it bounces of an occluder.
     *
     * @since 3.30.0
     */
    public Percent indirectLightBounceLossFactor() {
        return lightBounceLossFactor;
    }

    /**
     * Specify the intensity of indirect light that is cast when light hits occluders. Indirect light will be disabled
     * if intensity is set to is {@link Percent#zero()}. Default is 90%. Indirect light is very expensive,
     * turning it off increases fps significantly. Can also be turned off by setting {@link #setMaxLightBounces(int)}
     * to zero.
     *
     * @see #isIndirectLightEnabled()
     * @since 3.30.0
     */
    public GraphicsConfiguration setIndirectLightIntensity(final Percent intensity) {
        this.indirectLightIntensity = Objects.requireNonNull(intensity, "intensity must not be null");
        notifyListeners(GraphicsConfigurationEvent.ConfigurationProperty.INDIRECT_LIGHT_INTENSITY);
        return this;
    }

    /**
     * Returns the intensity of indirect light that is cast when light hits occluders. Indirect light will be disabled
     * if intensity is set to is {@link Percent#zero()}.
     *
     * @since 3.30.0
     */
    public Percent indirectLightIntensity() {
        return indirectLightIntensity;
    }

    /**
     * Returns the configured limit of cells used for rendering shockwaves. Default Value is 10,000.
     *
     * @since 3.24.0
     */
    public int shockwaveCellLimit() {
        return shockwaveCellLimit;
    }

    /**
     * Sets the limit of cells used for rendering shockwaves. Default Value is 10,000.
     *
     * @since 3.24.0
     */
    public GraphicsConfiguration setShockwaveCellLimit(final int shockwaveCellLimit) {
        Validate.range(shockwaveCellLimit, 500, 50_000, "shockwave cell limit must be in range 500 to 50,000");
        this.shockwaveCellLimit = shockwaveCellLimit;
        notifyListeners(GraphicsConfigurationEvent.ConfigurationProperty.SHOCKWAVE_CELL_LIMIT);
        return this;
    }

    /**
     * Returns {@code true} if light glow effects can cause lens flares on the camera (default is {@code true}).
     *
     * @since 3.8.0
     */
    public boolean isLensFlareEnabled() {
        return isLensFlareEnabled;
    }

    /**
     * Specifies, if light glow effects can cause lens flares on the camera (default is {@code true}).
     *
     * @since 3.8.0
     */
    public GraphicsConfiguration setLensFlareEnabled(final boolean isLensFlareEnabled) {
        notifyListeners(GraphicsConfigurationEvent.ConfigurationProperty.LENS_FLARE_ENABLED);
        this.isLensFlareEnabled = isLensFlareEnabled;
        return this;
    }

    /**
     * Toggles lens flare setting for light glows.
     *
     * @see #isLensFlareEnabled()
     * @since 3.8.0
     */
    public GraphicsConfiguration toggleLensFlare() {
        return setLensFlareEnabled(!isLensFlareEnabled);
    }

    /**
     * Returns the current {@link ShaderSetup}, that is used for all {@link Sprite sprites} that are drawn.
     *
     * @since 2.16.0
     */
    public ShaderSetup overlayShader() {
        return overlayShader;
    }

    /**
     * Disables the {@link ShaderSetup}, that is used for all {@link Sprite sprites} that are drawn.
     *
     * @since 2.16.0
     */
    public GraphicsConfiguration disableOverlayShader() {
        return setOverlayShader((ShaderSetup) null);
    }

    /**
     * Sets the {@link ShaderSetup}, that is used for all {@link Sprite sprites} that are drawn.
     *
     * @since 2.16.0
     */
    public GraphicsConfiguration setOverlayShader(final Supplier<ShaderSetup> shaderSetup) {
        return setOverlayShader(shaderSetup.get());
    }

    /**
     * Sets the {@link ShaderSetup}, that is used for all {@link Sprite sprites} that are drawn.
     *
     * @since 2.16.0
     */
    public GraphicsConfiguration setOverlayShader(final ShaderSetup shaderSetup) {
        this.overlayShader = shaderSetup;
        notifyListeners(GraphicsConfigurationEvent.ConfigurationProperty.OVERLAY_SHADER);
        return this;
    }

    /**
     * When turned on any interaction with {@link Light} will automatically enable {@link Light} rendering.
     *
     * @see #setLightEnabled(boolean)
     * @since 2.9.0
     */
    public GraphicsConfiguration setAutoEnableLight(final boolean isAutoEnableLight) {
        notifyListeners(GraphicsConfigurationEvent.ConfigurationProperty.AUTO_ENABLE_LIGHT);
        this.isAutoEnableLight = isAutoEnableLight;
        return this;
    }

    /**
     * Returns {@code true} when {@link Light} rendering is activated on light interaction.
     *
     * @since 2.9.0
     */
    public boolean isAutoEnableLight() {
        return isAutoEnableLight;
    }

    /**
     * Enables or disables {@link Light} rendering. Note that this property will be changed to {@code true} when {@link #isAutoEnableLight()}
     * is turned on and there is any interaction with {@link Light}.
     *
     * @see #setAutoEnableLight(boolean)
     * @since 2.9.0
     */
    public GraphicsConfiguration setLightEnabled(final boolean isLightEnabled) {
        notifyListeners(GraphicsConfigurationEvent.ConfigurationProperty.LIGHT_ENABLED);
        this.isLightEnabled = isLightEnabled;
        return this;
    }

    /**
     * Returns {@code true} when {@link Light} rendering is enabled.
     *
     * @since 2.9.0
     */
    public boolean isLightEnabled() {
        return isLightEnabled;
    }

    /**
     * Returns the configured light quality. Quality is used to calculate size of lightmap which may heavily impact
     * performance. Max quality will create a lightmap with height of the {@link #DEFAULT_RESOLUTION}. Higher
     * resolutions are not supported at the moment.
     *
     * @since 3.10.0
     */
    public Percent lightQuality() {
        return lightQuality;
    }

    /**
     * Configures the blur of the lightmap. 0 means no blur. Allows values up to 6.
     * Higher values cause lower {@link Loop#fps} but may improve visual quality
     * when using {@link Light}.
     *
     * @param lightBlur blur value from 0 (no blur) to 6.
     */
    public GraphicsConfiguration setLightmapBlur(final int lightBlur) {
        Validate.range(lightBlur, 0, 20, "lightmap blur must be in range 0 (no blur) to 20 (heavy blur)");
        this.lightBlur = lightBlur;
        notifyListeners(GraphicsConfigurationEvent.ConfigurationProperty.LIGHT_BLUR);
        return this;
    }

    /**
     * Returns the current blur of the lightmap.
     *
     * @see #setLightmapBlur(int)
     */
    public int lightmapBlur() {
        return lightBlur;
    }

    /**
     * Sets the antialiasing state. Antialiasing is used to draw with system fonts and
     * shapes. It doesn't enhance {@link Sprite} drawing. Using antialiasing may cost
     * some precious fps.
     */
    public GraphicsConfiguration setUseAntialiasing(final boolean useAntialiasing) {
        this.useAntialiasing = useAntialiasing;
        notifyListeners(GraphicsConfigurationEvent.ConfigurationProperty.ANTIALIASING);
        return this;
    }

    /**
     * Sets the current resolution. Be aware that not every resolution may be supported in fullscreen. Use
     * {@link Graphics#supportedResolutions()} to get a list of all supported fullscreen resolutions.
     *
     * @param width  the width of the resolution to set
     * @param height the height of the resolution to set
     */
    public GraphicsConfiguration setResolution(final int width, final int height) {
        setResolution(Size.of(width, height));
        return this;
    }

    /**
     * Sets the current resolution. Be aware that not every resolution may be supported in fullscreen. Use
     * {@link Graphics#supportedResolutions()} to get a list of all supported fullscreen resolutions.
     */
    public GraphicsConfiguration setResolution(final Size resolution) {
        this.resolution = requireNonNull(resolution, "resolution must not be null");
        notifyListeners(GraphicsConfigurationEvent.ConfigurationProperty.RESOLUTION);
        return this;
    }

    /**
     * Toggles fullscreen mode.
     */
    public GraphicsConfiguration toggleFullscreen() {
        return setFullscreen(!isFullscreen());
    }

    /**
     * Toggles the antialiasing on/off. Antialiasing is used to draw with system fonts
     * and shapes. It doesn't enhance {@link Sprite} drawing. Using antialiasing
     * costs some fps.
     */
    public GraphicsConfiguration toggleAntialiasing() {
        return setUseAntialiasing(!isUseAntialiasing());
    }

    /**
     * Sets fullscreen mode or get back to window mode.
     */
    public GraphicsConfiguration setFullscreen(final boolean fullscreen) {
        this.isFullscreen = fullscreen;
        notifyListeners(GraphicsConfigurationEvent.ConfigurationProperty.FULLSCREEN);
        return this;
    }

    /**
     * Adds a {@link GraphicsConfigurationListener}. The listener will be notified at any change of the
     * configuration.
     */
    public void addListener(final GraphicsConfigurationListener listener) {
        listeners.add(requireNonNull(listener, "listener must not be null"));
    }

    /**
     * Returns current resolution.
     */
    public Size resolution() {
        return resolution;
    }

    /**
     * Returns {@code true} if fullscreen is configured.
     */
    public boolean isFullscreen() {
        return isFullscreen;
    }

    /**
     * Returns true if antialiasing is used. Antialiasing is used to draw with system
     * fonts and shapes. It doesn't enhance {@link Sprite} drawing. Using
     * antialiasing costs some fps.
     */
    public boolean isUseAntialiasing() {
        return useAntialiasing;
    }

    /**
     * Configures the falloff of lights. Can be used to set a specific light mood.
     */
    public GraphicsConfiguration lightFalloff(final Percent lightFalloff) {
        this.lightFalloff = requireNonNull(lightFalloff, "light falloff must not be null");
        notifyListeners(GraphicsConfigurationEvent.ConfigurationProperty.LIGHT_FALLOFF);
        return this;
    }

    /**
     * Returns the falloff of lights.
     */
    public Percent lightFalloff() {
        return lightFalloff;
    }

    /**
     * Sets the background color used to prepare every new frame.
     *
     * @since 2.6.0
     */
    public GraphicsConfiguration setBackgroundColor(final Color backgroundColor) {
        this.backgroundColor = requireNonNull(backgroundColor, "background color must not be null");
        notifyListeners(GraphicsConfigurationEvent.ConfigurationProperty.BACKGROUND_COLOR);
        return this;
    }

    /**
     * Returns the background color used to prepare every new frame.
     *
     * @since 2.6.0
     */
    public Color backgroundColor() {
        return backgroundColor;
    }

    /**
     * Sets the light quality. Quality is used to calculate size of lightmap which may heavily impact performance.
     * Max quality will create a lightmap with height of the {@link #DEFAULT_RESOLUTION}. Higher resolutions are not
     * supported at the moment.
     *
     * @since 3.10.0
     */
    public GraphicsConfiguration setLightQuality(final Percent lightQuality) {
        this.lightQuality = requireNonNull(lightQuality, "light quality must not be null");
        notifyListeners(GraphicsConfigurationEvent.ConfigurationProperty.LIGHT_QUALITY);
        return this;
    }

    /**
     * The property is used to adjust graphic content to the current {@link #resolution()}.
     * Resolution scale will be 1.0 at the default resolution and will scale up and down adjusted to the
     * {@link #resolution()} height. {@link #resolution()} width will not affect the resolution scale.
     *
     * @since 3.26.0
     */
    public double resolutionScale() {
        return (double) resolution().height() / DEFAULT_RESOLUTION.height();
    }

    private void notifyListeners(final GraphicsConfigurationEvent.ConfigurationProperty changedProperty) {
        final var event = new GraphicsConfigurationEvent(this, changedProperty);
        for (final var listener : listeners) {
            listener.configurationChanged(event);
        }
    }
}

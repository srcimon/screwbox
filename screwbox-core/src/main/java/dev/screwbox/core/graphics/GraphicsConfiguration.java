package dev.screwbox.core.graphics;

import dev.screwbox.core.Percent;
import dev.screwbox.core.loop.Loop;
import dev.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * Configuration of major {@link Graphics} and rendering properties. Every change creates a
 * {@link GraphicsConfigurationEvent} that can be used to adjust to the new configuration.
 */
public class GraphicsConfiguration {

    /**
     * Resolution used when not changed during runtime.
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
    private int lightmapBlur = 3;
    private Percent lightFalloff = Percent.max();
    private Color backgroundColor = Color.BLACK;
    private ShaderSetup overlayShader = null;

    private int lightmapVerticalPixelCount = DEFAULT_RESOLUTION.height() / 4;

    //TODO double check md docs
    //TODO double check all javadocs
    //TODO use targetLightmapHeight as property and delete isAutoScaleLightmap & lightmapScale


    //TODO isAutoAdjustLightmapScaleEnabled
    //TODO move autoenablelight logic and autoAdjustLightmapScale to one location
    //TODO document

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

//TODO changelog -> auto adjust lightmap scale

    //TODO only expose lightmapPixels!!
    public int lightmapScale() {
        return (int) ((double)resolution.height() / lightmapVerticalPixelCount);
    }

    public int lightmapVerticalPixelCount() {
        return lightmapVerticalPixelCount;
    }

    /**
     * Configures the blur of the lightmap. 0 means no blur. Allows values up to 6.
     * Higher values cause lower {@link Loop#fps} but may improve visual quality
     * when using {@link Light}.
     *
     * @param lightmapBlur blur value from 0 (no blur) to 6.
     */
    public GraphicsConfiguration setLightmapBlur(final int lightmapBlur) {
        Validate.zeroOrPositive(lightmapBlur, "blur cannot be negative");
        Validate.max(lightmapBlur, 6, "blur only supports values 0 (no blur) to 6 (heavy blur)");
        this.lightmapBlur = lightmapBlur;
        notifyListeners(GraphicsConfigurationEvent.ConfigurationProperty.LIGHTMAP_BLUR);
        return this;
    }

    /**
     * Returns the current blur of the lightmap.
     *
     * @see #setLightmapBlur(int)
     */
    public int lightmapBlur() {
        return lightmapBlur;
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
        requireNonNull(resolution, "resolution must not be null");
        this.resolution = resolution;
        notifyListeners(GraphicsConfigurationEvent.ConfigurationProperty.RESOLUTION);
        return this;
    }

//TODO fix platformer quality light adjust dynamic by resolution

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
        notifyListeners(GraphicsConfigurationEvent.ConfigurationProperty.WINDOW_MODE);
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

    private void notifyListeners(final GraphicsConfigurationEvent.ConfigurationProperty changedProperty) {
        GraphicsConfigurationEvent event = new GraphicsConfigurationEvent(this, changedProperty);
        for (final var listener : listeners) {
            listener.configurationChanged(event);
        }
    }

    public GraphicsConfiguration setLightmapVerticalPixelCount(final int lightmapVerticalPixelCount) {
        //TODO validate
        //TODO event
        this.lightmapVerticalPixelCount = lightmapVerticalPixelCount;
        return this;
    }
}

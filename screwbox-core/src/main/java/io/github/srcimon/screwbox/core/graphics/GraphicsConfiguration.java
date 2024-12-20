package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Configuration of major {@link Graphics} and rendering properites. Every change creates a
 * {@link GraphicsConfigurationEvent} that can be used to adjust to the new configuration.
 */
public class GraphicsConfiguration {

    private final List<GraphicsConfigurationListener> listeners = new ArrayList<>();

    private Size resolution = Size.of(1280, 720);
    private boolean isFullscreen = false;
    private boolean useAntialiasing = false;
    private boolean isAutoEnableLight = true;
    private boolean isLightEnabled = false;
    private int lightmapBlur = 3;
    private int lightmapScale = 4;
    private Percent lightFalloff = Percent.max();
    private Color backgroundColor = Color.BLACK;

    //TODO apply this to example projects
    //TODO javadoc
    //TODO changelog
    public GraphicsConfiguration setAutoEnableLight(final boolean isAutoEnableLight) {
        notifyListeners(GraphicsConfigurationEvent.ConfigurationProperty.AUTO_ENABLE_LIGHT);
        this.isAutoEnableLight = isAutoEnableLight;
        return this;
    }

    //TODO javadoc
    //TODO changelog
    public boolean isAutoEnableLightRendering() {
        return isAutoEnableLight;
    }

    //TODO javadoc
    //TODO changelog
    public GraphicsConfiguration setLightEnabled(final boolean isLightEnabled) {
        notifyListeners(GraphicsConfigurationEvent.ConfigurationProperty.LIGHT_ENABLED);
        this.isLightEnabled = isLightEnabled;
        return this;
    }

    //TODO javadoc
    //TODO changelog
    public boolean isLightEnabled() {
        return isLightEnabled;
    }

    /**
     * Sets the resolution modifier for the light map. Higher values lower the
     * visual quality but massivly improve performance when using {@link Light}.
     * Default value is 4.
     *
     * @param lightmapScale in range from 1 to 6
     */
    public GraphicsConfiguration setLightmapScale(final int lightmapScale) {
        Validate.positive(lightmapScale, "lightmap scale must be positive");
        if (lightmapScale > 6) {
            throw new IllegalArgumentException("lightmap scale supports only values up to 6");
        }
        this.lightmapScale = lightmapScale;
        notifyListeners(GraphicsConfigurationEvent.ConfigurationProperty.LIGHTMAP_SCALE);
        return this;
    }

    /**
     * Returns the current lightmap resolution modifier.
     *
     * @see #setLightmapScale(int)
     */
    public int lightmapScale() {
        return lightmapScale;
    }

    /**
     * Configures the blur of the lightmap. 0 means no blur. Allowes values up to 6.
     * Higher values cause lower {@link Loop#fps} but may improve visual quality
     * when using {@link Light}.
     *
     * @param lightmapBlur blur value from 0 (no blur) to 6.
     */
    public GraphicsConfiguration setLightmapBlur(final int lightmapBlur) {
        Validate.zeroOrPositive(lightmapBlur, "blur cannot be negative");
        if (lightmapBlur > 6) {
            throw new IllegalArgumentException("blur only supports values 0 (no blur) to 6 (heavy blur)");
        }
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
     * Sets the antialising state. Antialising is used to draw with system fonts and
     * shapes. It doesn't enhance {@link Sprite} drawing. Using antialising may cost
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
        setFullscreen(!isFullscreen());
        return this;
    }

    /**
     * Toggles the antialising on/off. Antialising is used to draw with system fonts
     * and shapes. It doesn't enhance {@link Sprite} drawing. Using antialising
     * costs some fps.
     */
    public GraphicsConfiguration toggleAntialising() {
        setUseAntialiasing(!isUseAntialising());
        return this;
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
     * Returns true if antialising is used. Antialising is used to draw with system
     * fonts and shapes. It doesn't enhance {@link Sprite} drawing. Using
     * antialising costs some fps.
     */
    public boolean isUseAntialising() {
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

}

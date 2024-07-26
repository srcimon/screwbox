package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class GraphicsConfiguration {

    private final List<GraphicsConfigurationListener> listeners = new ArrayList<>();

    private Size resolution = Size.of(1280, 720);
    private boolean fullscreen = false;
    private boolean useAntialiasing = false;
    private int lightmapBlur = 2;
    private int lightmapScale = 4;
    private Percent lightFalloff = Percent.max();

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

    public GraphicsConfiguration setResolution(final int width, final int height) {
        setResolution(Size.of(width, height));
        return this;
    }

    public GraphicsConfiguration setResolution(final Size resolution) {
        this.resolution = requireNonNull(resolution, "resolution must not be null");
        notifyListeners(GraphicsConfigurationEvent.ConfigurationProperty.RESOLUTION);
        return this;
    }

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

    public GraphicsConfiguration setFullscreen(final boolean fullscreen) {
        this.fullscreen = fullscreen;
        notifyListeners(GraphicsConfigurationEvent.ConfigurationProperty.WINDOW_MODE);
        return this;
    }

    public void addListener(final GraphicsConfigurationListener listener) {
        listeners.add(requireNonNull(listener, "listener must not be null"));
    }

    public Size resolution() {
        return resolution;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    /**
     * Returns true if antialising is used. Antialising is used to draw with system
     * fonts and shapes. It doesn't enhance {@link Sprite} drawing. Using
     * antialising costs some fps.
     */
    public boolean isUseAntialising() {
        return useAntialiasing;
    }

    private void notifyListeners(final GraphicsConfigurationEvent.ConfigurationProperty changedProperty) {
        GraphicsConfigurationEvent event = new GraphicsConfigurationEvent(this, changedProperty);
        for (final var listener : listeners) {
            listener.configurationChanged(event);
        }
    }

    public GraphicsConfiguration lightFalloff(final Percent lightFalloff) {
        this.lightFalloff = lightFalloff;
        notifyListeners(GraphicsConfigurationEvent.ConfigurationProperty.LIGHT_FALLOFF);
        return this;
    }

    public Percent lightFalloff() {
        return lightFalloff;
    }
}

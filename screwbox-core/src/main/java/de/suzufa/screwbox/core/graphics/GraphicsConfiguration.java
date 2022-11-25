package de.suzufa.screwbox.core.graphics;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.graphics.GraphicsConfigurationListener.ConfigurationProperty;
import de.suzufa.screwbox.core.loop.Loop;

public class GraphicsConfiguration {

    private final List<GraphicsConfigurationListener> listeners = new ArrayList<>();

    private Dimension resolution = Dimension.of(960, 540);
    private boolean fullscreen = false;
    private boolean useAntialiasing = false;
    private int lightmapBlur = 4;
    private int lightmapResolution = 4;

    /**
     * Sets the resolution modifier for the light map. Higher values lower the
     * visual quality but massivly improve performance when using {@link Light}.
     * Default value is 4.
     * 
     * @param lightmapResolution in range from 1 to 6
     */
    public GraphicsConfiguration setLightmapResolution(final int lightmapResolution) {
        if (lightmapResolution < 1 || lightmapResolution > 6) {
            throw new IllegalArgumentException("valid range for lightmap resolution is 1 to 6");
        }
        this.lightmapResolution = lightmapResolution;
        notifyListeners(ConfigurationProperty.LIGHTMAP_RESOLUTION);
        return this;
    }

    /**
     * Returns the current lightmap resolution modifier.
     * 
     * @see #setLightmapResolution(int)
     */
    public int lightmapResolution() {
        return lightmapResolution;
    }

    /**
     * Configures the blur of the lightmap. 0 means no blur. Allowes values up to 6.
     * Higher values cause lower {@link Loop#fps} but may improve visual quality
     * when using {@link Light}.
     * 
     * @param lightmapBlur blur value from 0 (no blur) to 6.
     */
    public GraphicsConfiguration setLightmapBlur(final int lightmapBlur) {
        if (lightmapBlur < 0 || lightmapBlur > 6) {
            throw new IllegalArgumentException("valid range for lightmap blur is 0 (no blur) to 6 (heavy blur)");
        }
        this.lightmapBlur = lightmapBlur;
        notifyListeners(ConfigurationProperty.LIGHTMAP_BLUR);
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
        // TODO:non null
        this.useAntialiasing = useAntialiasing;
        notifyListeners(ConfigurationProperty.ANTIALIASING);
        return this;
    }

    public GraphicsConfiguration setResolution(final int width, final int height) {
        setResolution(Dimension.of(width, height));
        return this;
    }

    public GraphicsConfiguration setResolution(final Dimension resolution) {
        // TODO:non null
        this.resolution = resolution;
        notifyListeners(ConfigurationProperty.RESOLUTION);
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
        // TODO:non null
        this.fullscreen = fullscreen;
        notifyListeners(ConfigurationProperty.WINDOW_MODE);
        return this;
    }

    public void registerListener(final GraphicsConfigurationListener listener) {
        // TODO:non null
        listeners.add(listener);
    }

    public void removeListener(final GraphicsConfigurationListener listener) {
        // TODO:non null
        listeners.remove(listener);
    }

    public Dimension resolution() {
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

    private void notifyListeners(final ConfigurationProperty changedProperty) {
        for (final var listener : listeners) {
            listener.configurationChanged(changedProperty);
        }
    }

}

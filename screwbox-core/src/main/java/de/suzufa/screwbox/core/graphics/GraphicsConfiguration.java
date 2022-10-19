package de.suzufa.screwbox.core.graphics;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.graphics.GraphicsConfigurationListener.ConfigurationProperty;

public class GraphicsConfiguration {

    private final List<GraphicsConfigurationListener> listeners = new ArrayList<>();

    private Dimension resolution = Dimension.of(960, 540);
    private boolean fullscreen = false;
    private boolean useAntialiasing = false;

    /**
     * Sets the antialising state. Antialising is used to draw with system fonts and
     * shapes. It doesn't enhance {@link Sprite} drawing. Using antialising may cost
     * some precious fps.
     */
    public GraphicsConfiguration setUseAntialiasing(final boolean useAntialiasing) {
        this.useAntialiasing = useAntialiasing;
        notifyListeners(ConfigurationProperty.ANTIALIASING);
        return this;
    }

    public GraphicsConfiguration setResolution(final int width, final int height) {
        setResolution(Dimension.of(width, height));
        return this;
    }

    public GraphicsConfiguration setResolution(final Dimension resolution) {
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
        this.fullscreen = fullscreen;
        notifyListeners(ConfigurationProperty.WINDOW_MODE);
        return this;
    }

    public void registerListener(final GraphicsConfigurationListener listener) {
        listeners.add(listener);
    }

    public void removeListener(final GraphicsConfigurationListener listener) {
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

    private void notifyListeners(ConfigurationProperty changedProperty) {
        for (final var listener : listeners) {
            listener.configurationChanged(changedProperty);
        }
    }

}

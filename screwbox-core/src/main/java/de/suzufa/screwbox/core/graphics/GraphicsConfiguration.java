package de.suzufa.screwbox.core.graphics;

import java.util.ArrayList;
import java.util.List;

public final class GraphicsConfiguration {

    private final List<GraphicsConfigListener> listeners = new ArrayList<>();

    private Dimension resolution = Dimension.of(960, 540);
    private boolean fullscreen = false;
    private boolean useAntialiasing = false;

    public GraphicsConfiguration setUseAntialiasing(final boolean useAntialiasing) {
        this.useAntialiasing = useAntialiasing;
        notifyListeners();
        return this;
    }

    public GraphicsConfiguration setResolution(final int width, final int height) {
        setResolution(Dimension.of(width, height));
        return this;
    }

    public GraphicsConfiguration setResolution(final Dimension resolution) {
        this.resolution = resolution;
        notifyListeners();
        return this;
    }

    public GraphicsConfiguration toggleFullscreen() {
        setFullscreen(!isFullscreen());
        return this;
    }

    public GraphicsConfiguration setFullscreen(final boolean fullscreen) {
        this.fullscreen = fullscreen;
        notifyListeners();
        return this;
    }

    public void registerListener(final GraphicsConfigListener listener) {
        listeners.add(listener);
    }

    public void removeListener(final GraphicsConfigListener listener) {
        listeners.remove(listener);
    }

    public Dimension resolution() {
        return resolution;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public boolean isUseAntialising() {
        return useAntialiasing;
    }

    private void notifyListeners() {
        for (final var listener : listeners) {
            listener.configurationChanged();
        }
    }

}

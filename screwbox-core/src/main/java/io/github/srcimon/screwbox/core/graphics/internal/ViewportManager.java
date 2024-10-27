package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.SplitScreenOptions;
import io.github.srcimon.screwbox.core.graphics.Viewport;

import java.util.List;

public class ViewportManager {

    private final List<Viewport> defaultViewports;
    private final Viewport defaultViewport;
    private final Renderer renderer;
    private boolean isEnabled = false;

    public ViewportManager(final Viewport defaultViewport, final Renderer renderer) {
        this.renderer = renderer;
        this.defaultViewport = defaultViewport;
        this.defaultViewports = List.of(defaultViewport);
    }

    public boolean isSplitScreenEnabled() {
        return isEnabled;
    }

    public void enableSplitScreen(final SplitScreenOptions options) {
        createViewport();
        isEnabled = true;
    }

    private Viewport createViewport() {
        DefaultCanvas canvas = new DefaultCanvas(renderer, new ScreenBounds(0, 0, 500, 500));
        return new DefaultViewport(canvas, new DefaultCamera(canvas));
    }

    public void disableSplitScreen() {
        isEnabled = false;
    }

    public Viewport defaultViewport() {
        return defaultViewport;
    }

    public List<Viewport> activeViewports() {
        return defaultViewports;
    }
}

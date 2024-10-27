package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.SplitScreenOptions;
import io.github.srcimon.screwbox.core.graphics.Viewport;

import java.util.ArrayList;
import java.util.List;

public class ViewportManager {

    private final List<Viewport> defaultViewports;
    private final List<Viewport> splitScreenViewports = new ArrayList<>();
    private final Viewport defaultViewport;
    private final Renderer renderer;

    public ViewportManager(final Viewport defaultViewport, final Renderer renderer) {
        this.renderer = renderer;
        this.defaultViewport = defaultViewport;
        this.defaultViewports = List.of(defaultViewport);
    }

    public boolean isSplitScreenEnabled() {
        return !splitScreenViewports.isEmpty();
    }

    public void enableSplitScreen(final SplitScreenOptions options) {
        if (isSplitScreenEnabled()) {
            throw new IllegalStateException("split screen is already enabled");
        }
        for (int i = 0; i < options.screenCount(); i++) {
            int witdht = (int)(defaultViewport.canvas().width() / 2.0);
            splitScreenViewports.add(createViewport(new ScreenBounds(i * witdht, 0, witdht, defaultViewport.canvas().height())));
        }
    }

    private Viewport createViewport(ScreenBounds bounds) {
        DefaultCanvas canvas = new DefaultCanvas(renderer, bounds);
        DefaultCamera camera = new DefaultCamera(canvas);
        camera.setPosition(defaultViewport.camera().position());
        camera.setZoom(defaultViewport.camera().zoom());
        camera.setZoomRestriction(defaultViewport.camera().minZoom(), defaultViewport.camera().maxZoom());
        return new DefaultViewport(canvas, camera);
    }

    public void disableSplitScreen() {
        splitScreenViewports.clear();
    }

    public Viewport defaultViewport() {
        return defaultViewport;
    }

    public List<Viewport> activeViewports() {
        return isSplitScreenEnabled()
                ? splitScreenViewports
                : defaultViewports;
    }
}

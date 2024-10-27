package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.SplitScreenOptions;
import io.github.srcimon.screwbox.core.graphics.Viewport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ViewportManager {

    private final List<Viewport> defaultViewports;
    private final List<Viewport> splitScreenViewports = new ArrayList<>();
    private final Map<Integer, Viewport> viewportMap = new HashMap<>();
    private final Viewport defaultViewport;
    private final Renderer renderer;

    public ViewportManager(final Viewport defaultViewport, final Renderer renderer) {
        this.renderer = renderer;
        this.defaultViewport = defaultViewport;
        this.defaultViewports = List.of(defaultViewport);
        disableSplitScreen();
    }

    public boolean isSplitScreenEnabled() {
        return !splitScreenViewports.isEmpty();
    }

    public void enableSplitScreen(final SplitScreenOptions options) {
        if (isSplitScreenEnabled()) {
            throw new IllegalStateException("split screen is already enabled");
        }
        for (int i = 0; i < options.screenCount(); i++) {
            int witdht = (int) (defaultViewport.canvas().width() / options.screenCount() * 1.0);
            Viewport viewport = createViewport(new ScreenBounds(i * witdht, 0, witdht, defaultViewport.canvas().height()));
            splitScreenViewports.add(viewport);
            viewportMap.put(i, viewport);
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
        viewportMap.clear();
        viewportMap.put(0, defaultViewport);
    }

    public Viewport defaultViewport() {
        return defaultViewport;
    }

    public List<Viewport> activeViewports() {
        return isSplitScreenEnabled()
                ? splitScreenViewports
                : defaultViewports;
    }

    public Optional<Viewport> viewport(final int id) {
        return Optional.ofNullable(viewportMap.get(id));
    }
}

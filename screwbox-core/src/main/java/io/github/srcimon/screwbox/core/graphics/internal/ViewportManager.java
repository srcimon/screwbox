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
            splitScreenViewports.add(createViewport());
        }
    }

    private Viewport createViewport() {
        DefaultCanvas canvas = new DefaultCanvas(renderer, new ScreenBounds(0, 0, 500, 500));
        return new DefaultViewport(canvas, new DefaultCamera(canvas));
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

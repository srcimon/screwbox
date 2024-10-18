package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.Viewport;

import java.util.List;

public class ViewportManager {

    private final Viewport screenViewport;

    public ViewportManager(Viewport screenViewport) {
        this.screenViewport = screenViewport;
    }
    public List<Viewport> viewports() {
        return List.of(screenViewport);
    }
}

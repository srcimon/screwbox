package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Viewport;

import java.util.ArrayList;
import java.util.List;

public class ViewportManager {

    private final Viewport screenViewport;
    private final List<Viewport> additionalViewports = new ArrayList<>();
    private final Renderer renderer;
    private final DefaultScreen screen;

    public ViewportManager(Viewport screenViewport, Renderer renderer, DefaultScreen screen) {
        this.screenViewport = screenViewport;
        this.renderer = renderer;
        this.screen = screen;
    }

    public List<Viewport> viewports() {
        return additionalViewports.isEmpty() ?
                List.of(screenViewport)
                : additionalViewports;
    }

    public void enableSPlitscreen() {
        ScreenBounds clip = new ScreenBounds(0, 0, 600, 720);
        DefaultCanvas canvas = new DefaultCanvas(renderer, clip);
        Viewport left = new Viewport(canvas, new DefaultCamera(new DefaultWorld(canvas), screen));
        additionalViewports.add(left);

        ScreenBounds clip2 = new ScreenBounds(600, 0, 600, 720);
        DefaultCanvas canvas2 = new DefaultCanvas(renderer, clip2);
        Viewport right = new Viewport(canvas2, new DefaultCamera(new DefaultWorld(canvas2), screen));
        additionalViewports.add(right);
    }
}

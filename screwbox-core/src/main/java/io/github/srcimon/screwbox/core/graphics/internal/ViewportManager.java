package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Viewport;
import io.github.srcimon.screwbox.core.graphics.drawoptions.LineDrawOptions;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;

import java.util.ArrayList;
import java.util.List;

public class ViewportManager implements Updatable {

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
        int x = (int) (screenViewport.canvas().width() / 2.0);

        if(additionalViewports.isEmpty()) {
            ScreenBounds clip = new ScreenBounds(0, 0, x, 720);
            DefaultCanvas canvas = new DefaultCanvas(renderer, clip);
            var w = new DefaultWorld(canvas);
            Viewport left = new Viewport(canvas, new DefaultCamera(w, screen), w);
            additionalViewports.add(left);

            ScreenBounds clip2 = new ScreenBounds(x, 0, x, 720);
            DefaultCanvas canvas2 = new DefaultCanvas(renderer, clip2);
            var ws = new DefaultWorld(canvas2);
            Viewport right = new Viewport(canvas2, new DefaultCamera(ws, screen), ws);

            additionalViewports.add(right);
            left.camera().setZoom(4);
            right.camera().setZoom(4);
        }
    }

    @Override
    public void update() {
        if(!additionalViewports.isEmpty()) {
            int x = (int) (screenViewport.canvas().width() / 2.0);
            screenViewport.canvas().drawLine(Offset.at(x, 0), Offset.at(x, screenViewport.canvas().height()), LineDrawOptions.color(Color.BLACK).strokeWidth(4));
        }
    }
}

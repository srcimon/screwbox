package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.SplitScreenOptions;
import io.github.srcimon.screwbox.core.graphics.Viewport;
import io.github.srcimon.screwbox.core.graphics.drawoptions.LineDrawOptions;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ViewportManager implements Updatable {

    private final List<Viewport> defaultViewports;
    private final List<DefaultViewport> splitScreenViewports = new ArrayList<>();
    private final List<Viewport> splitScreenViewportsCorrectType = new ArrayList<>();
    private final Map<Integer, Viewport> viewportMap = new HashMap<>();
    private final Viewport defaultViewport;
    private final Renderer renderer;
private SplitScreenOptions options;

    public ViewportManager(final Viewport defaultViewport, final Renderer renderer) {
        this.renderer = renderer;
        this.defaultViewport = defaultViewport;
        this.defaultViewports = List.of(defaultViewport);
        disableSplitScreen();
        this.options = SplitScreenOptions.screenCount(1);
    }

    public boolean isSplitScreenEnabled() {
        return !splitScreenViewports.isEmpty();
    }

    public void enableSplitScreen(final SplitScreenOptions options) {
        if (isSplitScreenEnabled()) {
           disableSplitScreen();
        }
        for (int i = 0; i < options.screenCount(); i++) {
            DefaultViewport viewport = createViewport();
            splitScreenViewports.add(viewport);
            splitScreenViewportsCorrectType.add(viewport);
            viewportMap.put(i, viewport);
        }
        this.options = options;
        arangeViewports();
    }

    private DefaultViewport createViewport() {
        DefaultCanvas canvas = new DefaultCanvas(renderer, new ScreenBounds(0,0,1,1));
        DefaultCamera camera = new DefaultCamera(canvas);
        camera.setPosition(defaultViewport.camera().position());
        camera.setZoom(defaultViewport.camera().zoom());
        camera.setZoomRestriction(defaultViewport.camera().minZoom(), defaultViewport.camera().maxZoom());
        return new DefaultViewport(canvas, camera);
    }

    public void disableSplitScreen() {
        splitScreenViewports.clear();
        splitScreenViewportsCorrectType.clear();
        viewportMap.clear();
        viewportMap.put(0, defaultViewport);
        options = SplitScreenOptions.screenCount(1);
    }

    public Viewport defaultViewport() {
        return defaultViewport;
    }

    public List<Viewport> activeViewports() {
        return isSplitScreenEnabled()
                ? splitScreenViewportsCorrectType
                : defaultViewports;
    }

    public Optional<Viewport> viewport(final int id) {
        return Optional.ofNullable(viewportMap.get(id));
    }

    @Override
    public void update() {
        arangeViewports();
    }

    private void arangeViewports() {
        for (int i = 0; i < splitScreenViewports.size(); i++) {
            switch (options.arangement()) {
                case VERTICAL -> {
                    int witdht = (int) (defaultViewport.canvas().width() / splitScreenViewports.size() * 1.0);
                    splitScreenViewports.get(i).updateClip(new ScreenBounds(i * witdht, 0, witdht, defaultViewport.canvas().height()));
                    if (splitScreenViewports.size() > i - 1) {
                        defaultViewport.canvas().drawLine(
                                Offset.at(witdht * i, 0),
                                Offset.at(witdht * i, defaultViewport.canvas().height()),
                                LineDrawOptions.color(Color.BLACK).strokeWidth(4));
                    }
                }
                case HORIZONTAL -> {
                    int height = (int) (defaultViewport.canvas().height() / splitScreenViewports.size() * 1.0);
                    splitScreenViewports.get(i).updateClip(new ScreenBounds(0, i * height, defaultViewport.canvas().width(), height));
                    if (splitScreenViewports.size() > i - 1) {
                        defaultViewport.canvas().drawLine(
                                Offset.at(0, height * i),
                                Offset.at(defaultViewport.canvas().width() * i, height * i),
                                LineDrawOptions.color(Color.BLACK).strokeWidth(4));
                    }
                }
            }

        }
    }
}

package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.Camera;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.SplitScreenOptions;
import io.github.srcimon.screwbox.core.graphics.Viewport;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
        this.options = SplitScreenOptions.screens(1);
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
        DefaultCanvas canvas = new DefaultCanvas(renderer, new ScreenBounds(0, 0, 1, 1));
        DefaultCamera camera = new DefaultCamera(canvas);
        applyCameraSettingsToOtherCamera(defaultViewport.camera(), camera);
        return new DefaultViewport(canvas, camera);
    }

    private void applyCameraSettingsToOtherCamera(Camera from, Camera to) {
        to.setPosition(from.position());
        to.setZoom(from.zoom());
        to.setZoomRestriction(from.minZoom(), from.maxZoom());
    }

    public void disableSplitScreen() {
        if (!splitScreenViewports.isEmpty()) {
            applyCameraSettingsToOtherCamera(splitScreenViewports.getFirst().camera(), defaultViewport.camera());
        }
        splitScreenViewports.clear();
        splitScreenViewportsCorrectType.clear();
        viewportMap.clear();
        viewportMap.put(0, defaultViewport);
        options = SplitScreenOptions.screens(1);
    }

    public Viewport defaultViewport() {
        return defaultViewport;
    }

    public List<Viewport> activeViewports() {
        return isSplitScreenEnabled()
                ? splitScreenViewportsCorrectType
                : defaultViewports;
    }

    public Optional<Viewport> viewport(final int index) {
        return Optional.ofNullable(viewportMap.get(index));
    }

    private record Tupel<T>(T first, T second) {
    }

    @Override
    public void update() {
        arangeViewports();
        drawBorders();
    }

    private void drawBorders() {
        Set<Tupel<Offset>> alreadyDrawn = new HashSet<>();
        for (var viewport : splitScreenViewports) {
            var bounds = viewport.canvas().bounds();
            for (var side : List.of(
                    new Tupel<>(bounds.offset(), bounds.offset().addX(bounds.width())),
                    new Tupel<>(bounds.offset(), bounds.offset().addY(bounds.height())),
                    new Tupel<>(bounds.offset().addX(bounds.width()), bounds.offset().add(bounds.width(), bounds.height())),
                    new Tupel<>(bounds.offset().addY(bounds.height()), bounds.offset().add(bounds.width(), bounds.height())))
            ) {
                if (alreadyDrawn.add(side)) {
                    defaultViewport.canvas().drawLine(side.first(), side.second(), options.border());
                }
            }
        }
    }

    private void arangeViewports() {
        for (int i = 0; i < splitScreenViewports.size(); i++) {
            ScreenBounds viewportBounds = options.layout().calculateBounds(i, splitScreenViewports.size(), defaultViewport.canvas().bounds());
            splitScreenViewports.get(i).updateClip(viewportBounds);
        }
    }
}

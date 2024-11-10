package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.Camera;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.SplitscreenOptions;
import io.github.srcimon.screwbox.core.graphics.Viewport;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.isNull;

public class ViewportManager implements Updatable {

    private final List<Viewport> defaultViewports;
    private final List<DefaultViewport> splitScreenViewports = new ArrayList<>();
    private final List<Viewport> splitScreenViewportsCorrectType = new ArrayList<>();
    private final Map<Integer, Viewport> viewportMap = new HashMap<>();
    private final Viewport defaultViewport;
    private final Renderer renderer;
    private SplitscreenOptions options;

    public ViewportManager(final Viewport defaultViewport, final Renderer renderer) {
        this.renderer = renderer;
        this.defaultViewport = defaultViewport;
        this.defaultViewports = List.of(defaultViewport);
        disableSplitscreenMode();
    }

    public boolean isSplitscreenModeEnabled() {
        return !splitScreenViewports.isEmpty();
    }

    public void enableSplitscreenMode(final SplitscreenOptions options) {
        if (isSplitscreenModeEnabled()) {
            disableSplitscreenMode();
        }
        for (int i = 0; i < options.viewportCount(); i++) {
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

    private void applyCameraSettingsToOtherCamera(final Camera from, final Camera to) {
        to.setPosition(from.position());
        to.setZoom(from.zoom());
        to.setZoomRestriction(from.minZoom(), from.maxZoom());
    }

    public void disableSplitscreenMode() {
        if (!splitScreenViewports.isEmpty()) {
            applyCameraSettingsToOtherCamera(splitScreenViewports.getFirst().camera(), defaultViewport.camera());
        }
        splitScreenViewports.clear();
        splitScreenViewportsCorrectType.clear();
        viewportMap.clear();
        viewportMap.put(0, defaultViewport);
    }

    public Viewport defaultViewport() {
        return defaultViewport;
    }

    public List<Viewport> viewports() {
        return isSplitscreenModeEnabled()
                ? splitScreenViewportsCorrectType
                : defaultViewports;
    }

    public Optional<Viewport> viewport(final int index) {
        return Optional.ofNullable(viewportMap.get(index));
    }

    public Viewport primaryViewport() {
        return isSplitscreenModeEnabled()
                ? viewportMap.get(0)
                : defaultViewport;
    }

    private record Tupel<T>(T first, T second) {
    }

    @Override
    public void update() {
        arangeViewports();
    }

    public void renderSplitscreenBorders() {
        if (isNull(options) || isNull(options.borders())) {
            return;
        }
        final Set<Tupel<Offset>> alreadyDrawn = new HashSet<>();
        for (var viewport : splitScreenViewports) {
            final var canvasBounds = viewport.canvas().bounds();
            final var bounds = new ScreenBounds(canvasBounds.offset().substract(defaultViewport.canvas().offset()), canvasBounds.size());
            final Offset topRight = bounds.offset().addX(bounds.width());
            final Offset bottomLeft = bounds.offset().addY(bounds.height());
            final Offset bottomRight = bounds.offset().add(bounds.width(), bounds.height());
            for (var side : List.of(
                    new Tupel<>(bounds.offset(), topRight),
                    new Tupel<>(bounds.offset(), bottomLeft),
                    new Tupel<>(topRight, bottomRight),
                    new Tupel<>(bottomLeft, bottomRight))
            ) {
                if (!alreadyDrawn.add(side)) {
                    defaultViewport.canvas().drawLine(side.first(), side.second(), options.borders());
                }
            }
        }
    }

    private void arangeViewports() {
        for (int i = 0; i < splitScreenViewports.size(); i++) {
            final var viewportBounds = options.layout().calculateBounds(i, splitScreenViewports.size(), options.padding(), defaultViewport.canvas().bounds());
            splitScreenViewports.get(i).updateClip(viewportBounds);
        }
    }
}

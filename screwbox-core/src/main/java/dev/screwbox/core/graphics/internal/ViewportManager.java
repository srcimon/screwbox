package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.graphics.Camera;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.SplitScreenOptions;
import dev.screwbox.core.graphics.Viewport;
import dev.screwbox.core.graphics.internal.renderer.RenderPipeline;
import dev.screwbox.core.loop.internal.Updatable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ViewportManager implements Updatable {

    private final List<Viewport> defaultViewports;
    private final List<DefaultViewport> splitScreenViewports = new ArrayList<>();
    private final List<Viewport> splitScreenViewportsCorrectType = new ArrayList<>();
    private final Map<Integer, DefaultViewport> viewportMap = new HashMap<>();
    private final DefaultViewport defaultViewport;
    private final RenderPipeline renderPipeline;
    private SplitScreenOptions options;

    public ViewportManager(final DefaultViewport defaultViewport, final RenderPipeline renderPipeline) {
        this.renderPipeline = renderPipeline;
        this.defaultViewport = defaultViewport;
        this.defaultViewports = List.of(defaultViewport);
        disableSplitscreenMode();
    }

    public Viewport calculateHoverViewport(final Offset offset) {
        if (!isSplitscreenModeEnabled()) {
            return defaultViewport;
        }
        for (final var viewport : viewports()) {
            final Offset fixedOffset = offset.add(defaultViewport.canvas().offset());
            if (viewport.canvas().bounds().contains(fixedOffset)) {
                return viewport;
            }
        }
        return defaultViewport;
    }


    public boolean isSplitscreenModeEnabled() {
        return !splitScreenViewports.isEmpty();
    }

    public void enableSplitscreenMode(final SplitScreenOptions options) {
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
        arrangeViewports();
        renderPipeline.skipFrames();
    }

    private DefaultViewport createViewport() {
        final DefaultCanvas canvas = new DefaultCanvas(renderPipeline.renderer(), new ScreenBounds(0, 0, 1, 1));
        final DefaultCamera camera = new DefaultCamera(canvas);
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

    @Override
    public void update() {
        arrangeViewports();
        for(final var viewport : splitScreenViewports) {
            viewport.camera().update();
        }
    }

    private void arrangeViewports() {
        for (int i = 0; i < splitScreenViewports.size(); i++) {
            final var viewportBounds = options.layout().calculateBounds(i, splitScreenViewports.size(), options.padding(), defaultViewport.canvas().bounds());
            splitScreenViewports.get(i).updateClip(viewportBounds);
        }
    }
}

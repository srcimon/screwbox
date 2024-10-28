package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.Camera;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.SplitScreenOptions;
import io.github.srcimon.screwbox.core.graphics.Viewport;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;
import io.github.srcimon.screwbox.core.utils.Tupel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

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
        this.options = SplitScreenOptions.horizontal(1);
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
        if(!splitScreenViewports.isEmpty()) {
            applyCameraSettingsToOtherCamera(splitScreenViewports.getFirst().camera(), defaultViewport.camera());
        }
        splitScreenViewports.clear();
        splitScreenViewportsCorrectType.clear();
        viewportMap.clear();
        viewportMap.put(0, defaultViewport);
        options = SplitScreenOptions.horizontal(1);
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
        if(!splitScreenViewports.isEmpty()) {
            List<Tupel<Offset>> duplicates = new ArrayList<>();
            Set<Tupel<Offset>> borders = new HashSet<>();
            for (var viewport : splitScreenViewports) {
                var bounds = viewport.canvas().bounds();
                List<Tupel<Offset>> sides = new ArrayList<>();
                sides.add(new Tupel<>(bounds.offset(), bounds.offset().addX(bounds.width())));
                sides.add(new Tupel<>(bounds.offset(), bounds.offset().addY(bounds.height())));
                sides.add(new Tupel<>(bounds.offset().addX(bounds.width()), bounds.offset().add(bounds.width(), bounds.height())));
                sides.add(new Tupel<>(bounds.offset().addY(bounds.height()), bounds.offset().add(bounds.width(), bounds.height())));
                for(var side : sides) {
                    if(!borders.add(side)) {
                        duplicates.add(side);
                    }
                }
            }
            for(var side : duplicates) {
                defaultViewport.canvas().drawLine(side.first(), side.second(), options.border());
            }
        }
    }

    private void arangeViewports() {

        for (int i = 0; i < splitScreenViewports.size(); i++) {
            splitScreenViewports.get(i).updateClip(    options.layout().calculateBounds(i, splitScreenViewports.size(), defaultViewport.canvas().bounds()));
        }
    }
}

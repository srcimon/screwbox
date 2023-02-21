package io.github.simonbas.screwbox.core.graphics.internal;

import io.github.simonbas.screwbox.core.Vector;
import io.github.simonbas.screwbox.core.graphics.Dimension;
import io.github.simonbas.screwbox.core.graphics.Graphics;
import io.github.simonbas.screwbox.core.graphics.GraphicsConfiguration;
import io.github.simonbas.screwbox.core.graphics.Window;
import io.github.simonbas.screwbox.core.graphics.*;
import io.github.simonbas.screwbox.core.loop.internal.Updatable;

import java.awt.Font;
import java.awt.*;
import java.util.List;

import static java.util.Arrays.asList;

public class DefaultGraphics implements Graphics, Updatable {

    private final GraphicsConfiguration configuration;
    private final DefaultWindow window;
    private final DefaultWorld world;
    private final DefaultLight light;
    private final DefaultScreen screen;

    public DefaultGraphics(final GraphicsConfiguration configuration,
            final DefaultScreen screen,
            final DefaultWindow window,
            final DefaultWorld world,
            final DefaultLight light) {
        this.configuration = configuration;
        this.window = window;
        this.light = light;
        this.world = world;
        this.screen = screen;
    }

    @Override
    public GraphicsConfiguration configuration() {
        return configuration;
    }

    @Override
    public Window window() {
        return window;
    }

    @Override
    public World world() {
        return world;
    }

    @Override
    public double updateCameraZoomBy(final double delta) {
        return world.updateCameraZoom(world.wantedZoom() + delta);
    }

    @Override
    public double updateCameraZoom(final double zoom) {
        return world.updateCameraZoom(zoom);
    }

    @Override
    public Graphics updateCameraPosition(final Vector position) {
        world.updateCameraPosition(position);
        return this;
    }

    @Override
    public Vector cameraPosition() {
        return world.cameraPosition();
    }

    @Override
    public double cameraZoom() {
        return world.cameraZoom();
    }

    @Override
    public Vector worldPositionOf(final Offset offset) {
        return world.toPosition(offset);
    }

    @Override
    public Offset windowPositionOf(final Vector position) {
        return world.toOffset(position);
    }

    @Override
    public List<Dimension> supportedResolutions() {
        return window.supportedResolutions();
    }

    @Override
    public List<Dimension> supportedResolutions(final AspectRatio ratio) {
        return supportedResolutions().stream()
                .filter(ratio::matches)
                .toList();
    }

    @Override
    public List<String> availableFonts() {
        final var allFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
        return asList(allFonts).stream()
                .map(Font::getFontName)
                .toList();
    }

    @Override
    public void update() {
        screen.updateScreen(configuration.isUseAntialising());
        world.recalculateVisibleArea();
    }

    @Override
    public Graphics restrictZoomRangeTo(final double min, final double max) {
        world.restrictZoomRangeTo(min, max);
        return this;
    }

    @Override
    public Dimension currentResolution() {
        return window.currentResolution();
    }

    @Override
    public Light light() {
        return light;
    }

    @Override
    public Screen screen() {
        return screen;
    }

}

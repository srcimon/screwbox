package de.suzufa.screwbox.core.graphics.internal;

import static java.util.Arrays.asList;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.List;

import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.graphics.AspectRatio;
import de.suzufa.screwbox.core.graphics.Dimension;
import de.suzufa.screwbox.core.graphics.Graphics;
import de.suzufa.screwbox.core.graphics.GraphicsConfiguration;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Window;
import de.suzufa.screwbox.core.graphics.World;
import de.suzufa.screwbox.core.graphics.light.Light;
import de.suzufa.screwbox.core.graphics.light.internal.DefaultLight;
import de.suzufa.screwbox.core.loop.internal.Updatable;

public class DefaultGraphics implements Graphics, Updatable {

    private final GraphicsConfiguration configuration;
    private final DefaultWindow window;
    private final DefaultWorld world;
    private final DefaultLight light;

    public DefaultGraphics(final GraphicsConfiguration configuration, final DefaultWindow window,
            final DefaultLight light) {
        this.configuration = configuration;
        this.window = window;
        this.light = light;
        world = new DefaultWorld(window);
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
        window.updateScreen(configuration.isUseAntialising());
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

}

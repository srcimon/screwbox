package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Line;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Graphics;
import io.github.srcimon.screwbox.core.graphics.GraphicsConfiguration;
import io.github.srcimon.screwbox.core.graphics.*;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;
import io.github.srcimon.screwbox.core.physics.Borders;

import java.awt.Font;
import java.awt.*;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.Comparator.reverseOrder;

public class DefaultGraphics implements Graphics, Updatable {

    private final GraphicsConfiguration configuration;
    private final DefaultWorld world;
    private final DefaultLight light;
    private final DefaultScreen screen;
    private final GraphicsDevice graphicsDevice;

    public DefaultGraphics(final GraphicsConfiguration configuration,
                           final DefaultScreen screen,
                           final DefaultWorld world,
                           final DefaultLight light,
                           final GraphicsDevice graphicsDevice) {
        this.configuration = configuration;
        this.light = light;
        this.world = world;
        this.screen = screen;
        this.graphicsDevice = graphicsDevice;
    }

    @Override
    public GraphicsConfiguration configuration() {
        return configuration;
    }

    @Override
    public World world() {
        return world;
    }

    @Override
    public double updateZoomRelative(final double delta) {
        return world.updateZoom(world.wantedZoom() + delta);
    }

    @Override
    public double updateZoom(final double zoom) {
        return world.updateZoom(zoom);
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
    public Vector toPosition(final Offset offset) {
        return world.toPosition(offset);
    }

    @Override
    public Offset toOffset(final Vector position) {
        return world.toOffset(position);
    }

    @Override
    public List<Size> supportedResolutions() {
        return stream(graphicsDevice.getDisplayModes())
                .map(this::toDimension)
                .distinct()
                .sorted(reverseOrder())
                .toList();
    }

    private Size toDimension(final DisplayMode screenSize) {
        return Size.of(screenSize.getWidth(), screenSize.getHeight());
    }

    @Override
    public List<Size> supportedResolutions(final AspectRatio ratio) {
        return supportedResolutions().stream()
                .filter(ratio::matches)
                .toList();
    }

    @Override
    public List<String> availableFonts() {
        final var allFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
        return Stream.of(allFonts)
                .map(Font::getFontName)
                .toList();

    }

    @Override
    public void update() {
        screen.updateScreen(configuration.isUseAntialising());
        light.update();
        world.recalculateVisibleArea();
    }

    @Override
    public Graphics restrictZoomRangeTo(final double min, final double max) {
        world.restrictZoomRangeTo(min, max);
        return this;
    }

    @Override
    public Size currentResolution() {
        return toDimension(graphicsDevice.getDisplayMode());
    }

    @Override
    public Light light() {
        return light;
    }

    @Override
    public Screen screen() {
        return screen;
    }

    @Override
    public Vector updateCameraPositionWithinVisibleBounds(final Vector position, final Bounds bounds) {
        var x = Bounds.atPosition(
                bounds.position(),
                Math.max(0, bounds.width() - world.visibleArea().width()),
                Math.max(0, bounds.height() - world.visibleArea().height()));
        return updateCameraPositionWithinBounds(position, x);//TODO FIX and test
    }

    //TODO TEST and refactor
    @Override
    public Vector updateCameraPositionWithinBounds(final Vector position, final Bounds bounds) {
        if(bounds.contains(position)) {
            world.updateCameraPosition(position);
        } else {
            var borders = Borders.ALL.extractFrom(bounds);
            if(bounds.contains(world.cameraPosition())) {
                var movement = Line.between(world.cameraPosition(), position);
                var allIntersections = movement.intersections(borders);
                if(allIntersections.isEmpty()) {
                    world.updateCameraPosition(position);
                } else {
                    var nearestIntersection = position.nearestOf(allIntersections);
                    world.updateCameraPosition(nearestIntersection);
                }//TODO move x and y separately

            } else {
                var movement = Line.between(position, bounds.position());
                var allIntersections = movement.intersections(borders);
                if(allIntersections.isEmpty()) {
                    world.updateCameraPosition(position);
                } else {
                    var nearestIntersection = position.nearestOf(allIntersections);
                    world.updateCameraPosition(nearestIntersection);
                }
            }
        }
        return world.cameraPosition();
    }
}

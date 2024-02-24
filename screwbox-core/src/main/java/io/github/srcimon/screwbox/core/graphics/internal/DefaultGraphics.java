package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.graphics.Graphics;
import io.github.srcimon.screwbox.core.graphics.GraphicsConfiguration;
import io.github.srcimon.screwbox.core.graphics.*;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;
import io.github.srcimon.screwbox.core.utils.MathUtil;

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
    public Vector moveCameraWithinVisualBounds(Vector delta, Bounds bounds) {
        double width = bounds.width() - world.visibleArea().width();
        double height = bounds.height() - world.visibleArea().height();
//        if(width <= 0 || height <= 0) {
//            System.out.println(width);
//            return cameraPosition();
//        }
        final var visualBounds = Bounds.atPosition(bounds.position(),
                Math.max(0.1, width),
                Math.max(0.1, height));

        final double movementX = MathUtil.clamp(
                visualBounds.minX() -cameraPosition().x(),
                delta.x(),
                visualBounds.maxX()-cameraPosition().x());

        final double movementY = MathUtil.clamp(
                visualBounds.minY()-cameraPosition().y(),
                delta.y(),
                visualBounds.maxY()-cameraPosition().y());

        moveCamera(Vector.$(movementX, movementY));
        return cameraPosition();
    }

}

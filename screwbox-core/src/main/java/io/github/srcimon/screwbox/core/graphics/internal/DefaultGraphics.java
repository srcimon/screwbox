package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.Graphics;
import io.github.srcimon.screwbox.core.graphics.GraphicsConfiguration;
import io.github.srcimon.screwbox.core.graphics.*;
import io.github.srcimon.screwbox.core.graphics.internal.renderer.AsyncRenderer;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;

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
    private final DefaultCamera camera;
    private final AsyncRenderer asyncRenderer;
    private final Viewport viewport;

    public DefaultGraphics(final GraphicsConfiguration configuration,
                           final DefaultScreen screen,
                           final DefaultWorld world,
                           final DefaultLight light,
                           final GraphicsDevice graphicsDevice,
                           final DefaultCamera camera,
                           final AsyncRenderer asyncRenderer,
                           final Viewport viewport) {
        this.configuration = configuration;
        this.light = light;
        this.world = world;
        this.screen = screen;
        this.graphicsDevice = graphicsDevice;
        this.camera = camera;
        this.asyncRenderer = asyncRenderer;
        this.viewport = viewport;
    }

    @Override
    public Bounds visibleArea() {
        return viewport.visibleArea();
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
    public Camera camera() {
        return camera;
    }

    @Override
    public Canvas canvas() {
        return viewport.canvas();
    }

    @Override
    public Vector toWorld(final Offset offset) {
        return viewport.toWorld(offset);
    }

    @Override
    public int toCanvas(double worldDistance) {
        return viewport.toCanvas(worldDistance);
    }

    @Override
    public ScreenBounds toCanvas(final Bounds bounds) {
        return viewport.toCanvas(bounds);
    }

    @Override
    public ScreenBounds toCanvas(final Bounds bounds, final double parallaxX, final double parallaxY) {
        return viewport.toCanvas(bounds, parallaxX, parallaxY);
    }

    @Override
    public Offset toCanvas(final Vector position) {
        return viewport.toCanvas(position);
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
    public Duration renderDuration() {
        return asyncRenderer.renderDuration();
    }

    @Override
    public void update() {
        screen.updateScreen(configuration.isUseAntialising());
        light.update();
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
    public Canvas createCanvas(final Offset offset, final Size size) {
        return screen.createCanvas(offset, size);
    }
}

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
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.Comparator.reverseOrder;

public class DefaultGraphics implements Graphics, Updatable {

    private final GraphicsConfiguration configuration;
    private final DefaultWorld world;
    private final DefaultLight light;
    private final DefaultScreen screen;
    private final GraphicsDevice graphicsDevice;
    private final AsyncRenderer asyncRenderer;
    private final ViewportManager viewportManager;
    private final AttentionFocus attentionFocus;

    public DefaultGraphics(final GraphicsConfiguration configuration,
                           final DefaultScreen screen,
                           final DefaultLight light,
                           final GraphicsDevice graphicsDevice,
                           final AsyncRenderer asyncRenderer,
                           final ViewportManager viewportManager,
                           final AttentionFocus attentionFocus) {
        this.configuration = configuration;
        this.light = light;
        this.screen = screen;
        this.graphicsDevice = graphicsDevice;
        this.asyncRenderer = asyncRenderer;
        this.viewportManager = viewportManager;
        this.attentionFocus = attentionFocus;
        this.world = new DefaultWorld(viewportManager);
    }

    @Override
    public Bounds visibleArea() {
        return viewportManager.defaultViewport().visibleArea();
    }

    @Override
    public Graphics enableSplitScreen(final SplitScreenOptions options) {
        viewportManager.enableSplitScreen(options);
        return this;
    }

    @Override
    public Graphics disableSplitScreen() {
        viewportManager.disableSplitScreen();
        return this;
    }

    @Override
    public Optional<Viewport> vieport(final int index) {
        return viewportManager.viewport(index);
    }

    @Override
    public boolean isSplitScreenEnabled() {
        return viewportManager.isSplitScreenEnabled();
    }

    @Override
    public List<Viewport> activeViewports() {
        return viewportManager.activeViewports();
    }

    @Override
    public Viewport primaryViewport() {
        return viewportManager.primaryViewport();
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
        return viewportManager.defaultViewport().camera();
    }

    @Override
    public Canvas canvas() {
        return viewportManager.defaultViewport().canvas();
    }

    @Override
    public Vector toWorld(final Offset offset) {
        return viewportManager.defaultViewport().toWorld(offset);
    }

    @Override
    public int toCanvas(double distance) {
        return viewportManager.defaultViewport().toCanvas(distance);
    }

    @Override
    public ScreenBounds toCanvas(final Bounds bounds) {
        return viewportManager.defaultViewport().toCanvas(bounds);
    }

    @Override
    public ScreenBounds toCanvas(final Bounds bounds, final double parallaxX, final double parallaxY) {
        return viewportManager.defaultViewport().toCanvas(bounds, parallaxX, parallaxY);
    }

    @Override
    public Offset toCanvas(final Vector position) {
        return viewportManager.defaultViewport().toCanvas(position);
    }

    @Override
    public List<Size> supportedResolutions() {
        return stream(graphicsDevice.getDisplayModes())
                .map(this::toDimension)
                .distinct()
                .sorted(reverseOrder())
                .toList();
    }

    @Override
    public List<Size> supportedResolutions(final AspectRatio ratio) {
        return supportedResolutions().stream()
                .filter(ratio::matches)
                .toList();
    }

    @Override
    public List<String> availableFonts() {
        return Stream.of(GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts())
                .map(Font::getFontName)
                .toList();
    }

    @Override
    public Duration renderDuration() {
        return asyncRenderer.renderDuration();
    }

    @Override
    public boolean isWithinDistanceToVisibleArea(final Vector position, final double distance) {
        return attentionFocus.isWithinDistanceToVisibleArea(position, distance);
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

    private Size toDimension(final DisplayMode screenSize) {
        return Size.of(screenSize.getWidth(), screenSize.getHeight());
    }
}

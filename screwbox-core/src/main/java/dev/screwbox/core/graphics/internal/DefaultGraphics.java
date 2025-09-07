package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Duration;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.*;
import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.Graphics;
import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.internal.renderer.RenderPipeline;
import dev.screwbox.core.loop.internal.Updatable;

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
    private final RenderPipeline renderPipeline;
    private final ViewportManager viewportManager;
    private final AttentionFocus attentionFocus;

    public DefaultGraphics(final GraphicsConfiguration configuration,
                           final DefaultScreen screen,
                           final DefaultLight light,
                           final GraphicsDevice graphicsDevice,
                           final RenderPipeline renderPipeline,
                           final ViewportManager viewportManager,
                           final AttentionFocus attentionFocus) {
        this.configuration = configuration;
        this.light = light;
        this.screen = screen;
        this.graphicsDevice = graphicsDevice;
        this.renderPipeline = renderPipeline;
        this.viewportManager = viewportManager;
        this.attentionFocus = attentionFocus;
        this.world = new DefaultWorld(viewportManager);
    }

    @Override
    public Bounds visibleArea() {
        return viewportManager.defaultViewport().visibleArea();
    }

    @Override
    public Viewport viewportAt(final Offset offset) {
        return viewportManager.calculateHoverViewport(offset);
    }

    @Override
    public Graphics enableSplitScreenMode(final SplitScreenOptions options) {
        viewportManager.enableSplitscreenMode(options);
        return this;
    }

    @Override
    public Graphics disableSplitScreenMode() {
        viewportManager.disableSplitscreenMode();
        return this;
    }

    @Override
    public Optional<Viewport> viewport(final int index) {
        return viewportManager.viewport(index);
    }

    @Override
    public boolean isSplitScreenModeEnabled() {
        return viewportManager.isSplitscreenModeEnabled();
    }

    @Override
    public List<Viewport> viewports() {
        return viewportManager.viewports();
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
        return renderPipeline.renderDuration();
    }

    @Override
    public boolean isWithinDistanceToVisibleArea(final Vector position, final double distance) {
        return attentionFocus.isWithinDistanceToVisibleArea(position, distance);
    }

    @Override
    public void update() {
        screen.updateScreen(configuration.isUseAntialiasing());
        light.update();
    }

    @Override
    public Size resolution() {
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

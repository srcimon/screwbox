package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Vector;
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
    private final ViewportSupport viewportSupport;

    public DefaultGraphics(final GraphicsConfiguration configuration,
                           final DefaultScreen screen,
                           final DefaultWorld world,
                           final DefaultLight light,
                           final GraphicsDevice graphicsDevice,
                           final DefaultCamera camera,
                           final AsyncRenderer asyncRenderer,
                           final ViewportSupport viewportSupport) {
        this.configuration = configuration;
        this.light = light;
        this.world = world;
        this.screen = screen;
        this.graphicsDevice = graphicsDevice;
        this.camera = camera;
        this.asyncRenderer = asyncRenderer;
        this.viewportSupport = viewportSupport;
    }

    @Override
    public List<Viewport> activeViewports() {
        return viewportSupport.isSplitscreenActive()
                ? viewportSupport.viewports()
                : List.of(screen);
    }

    @Override
    public Graphics enableSplitscreen() {
        viewportSupport.setSplitscreenEnabled(true);
        return this;
    }

    @Override
    public Graphics disableSplitScreen() {
        viewportSupport.setSplitscreenEnabled(false);
        return this;
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
    public Vector toPosition(final Offset offset) {
        return world.toPosition(offset);
    }

    @Override
    public ScreenBounds toScreen(final Bounds bounds) {
        return world.toScreen(bounds);
    }

    @Override
    public ScreenBounds toScreenUsingParallax(final Bounds bounds, final double parallaxX, final double parallaxY) {
        return world.toScreen(bounds, parallaxX, parallaxY);
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
    public Duration renderDuration() {
        return asyncRenderer.renderDuration();
    }

    @Override
    public void update() {
        world.updateCameraPosition(camera.focus());
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
}

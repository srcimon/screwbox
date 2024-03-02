package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.graphics.AspectRatio;
import io.github.srcimon.screwbox.core.graphics.Graphics;
import io.github.srcimon.screwbox.core.graphics.GraphicsConfiguration;
import io.github.srcimon.screwbox.core.graphics.Light;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.World;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;
import io.github.srcimon.screwbox.core.utils.MathUtil;

import java.awt.*;
import java.util.List;
import java.util.stream.Stream;

import static io.github.srcimon.screwbox.core.Vector.$;
import static java.util.Arrays.stream;
import static java.util.Comparator.reverseOrder;

public class DefaultGraphics implements Graphics, Updatable {

    private final GraphicsConfiguration configuration;
    private final DefaultWorld world;
    private final DefaultLight light;
    private final DefaultScreen screen;
    private final GraphicsDevice graphicsDevice;
    private final Camera camera;

    public DefaultGraphics(final GraphicsConfiguration configuration,
                           final DefaultScreen screen,
                           final DefaultWorld world,
                           final DefaultLight light,
                           final GraphicsDevice graphicsDevice,
                           final Camera camera) {
        this.configuration = configuration;
        this.light = light;
        this.world = world;
        this.screen = screen;
        this.graphicsDevice = graphicsDevice;
        this.camera = camera;
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
        var actualZoom = camera.updateZoom(camera.wantedZoom() + delta);
        world.updateZoom(actualZoom);
        return actualZoom;
    }

    @Override
    public Graphics addCameraShake(double strength, Duration interval, Duration duration) {
        camera.addTimedShake(strength,interval,duration);
        return this;
    }

    @Override
    public double updateZoom(final double zoom) {
        var actualZoom = camera.updateZoom(zoom);
        world.updateZoom(actualZoom);
        world.updateZoom(zoom);
        return actualZoom;
    }

    @Override
    public Graphics updateCameraPosition(final Vector position) {
        camera.updatePosition(position);
        world.updateCameraPosition(camera.focus());

        return this;
    }

    @Override
    public Vector cameraPosition() {
        return camera.position();
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
    }

    @Override
    public Graphics restrictZoomRangeTo(final double min, final double max) {
        camera.restrictZoomRangeTo(min, max);
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
    public Vector moveCameraWithinVisualBounds(final Vector delta, final Bounds bounds) {
        final var legalPostionArea = Bounds.atPosition(bounds.position(),
                Math.max(1, bounds.width() - world.visibleArea().width()),
                Math.max(1, bounds.height() - world.visibleArea().height()));

        final double movementX = MathUtil.clamp(
                legalPostionArea.minX() - cameraPosition().x(),
                delta.x(),
                legalPostionArea.maxX() - cameraPosition().x());

        final double movementY = MathUtil.clamp(
                legalPostionArea.minY() - cameraPosition().y(),
                delta.y(),
                legalPostionArea.maxY() - cameraPosition().y());

        moveCamera($(movementX, movementY));
        return cameraPosition();
    }

}

package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.graphics.Camera;
import io.github.srcimon.screwbox.core.graphics.CameraShakeOptions;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;
import io.github.srcimon.screwbox.core.utils.MathUtil;
import io.github.srcimon.screwbox.core.utils.Noise;

import static io.github.srcimon.screwbox.core.Vector.$;
import static java.util.Objects.nonNull;

public class DefaultCamera implements Camera, Updatable {

    private final DefaultWorld world;
    private Vector shake = Vector.zero();
    private Vector position = Vector.zero();
    private double zoom = 2;
    private double requestedZoom = zoom;
    private double minZoom = 2;
    private double maxZoom = 5;
    private Time start = null;
    private CameraShakeOptions activeShake = null;
    private Noise xNoise = Noise.variableInterval(Duration.ofMillis(200));
    private Noise yNoise = Noise.variableInterval(Duration.ofMillis(200));

    public DefaultCamera(DefaultWorld world) {
        this.world = world;
    }

    @Override
    public Camera updatePosition(Vector position) {
        this.position = position;
        world.updateCameraPosition(focus());
        return this;
    }

    @Override
    public Vector position() {
        return position;
    }

    @Override
    public Vector focus() {
        return position.add(shake);
    }

    @Override
    public Camera updateZoomRestriction(final double min, final double max) {
        if (min <= 0) {
            throw new IllegalArgumentException("min zoom must be positive");
        }
        if (min > max) {
            throw new IllegalArgumentException("max zoom must not be lower than min zoom");
        }
        this.minZoom = min;
        this.maxZoom = max;
        return this;
    }

    @Override
    public double updateZoom(final double zoom) {
        this.requestedZoom = MathUtil.clamp(minZoom, zoom, maxZoom);
        final double actualZoomValue = Math.floor(requestedZoom * 16.0) / 16.0;
        this.zoom = actualZoomValue;
        world.updateZoom(this.zoom);
        return actualZoomValue;
    }

    @Override
    public Vector moveWithinVisualBounds(final Vector delta, final Bounds bounds) {
        final var legalPostionArea = Bounds.atPosition(bounds.position(),
                Math.max(1, bounds.width() - world.visibleArea().width()),
                Math.max(1, bounds.height() - world.visibleArea().height()));

        final double movementX = MathUtil.clamp(
                legalPostionArea.minX() - position().x(),
                delta.x(),
                legalPostionArea.maxX() - position().x());

        final double movementY = MathUtil.clamp(
                legalPostionArea.minY() - position().y(),
                delta.y(),
                legalPostionArea.maxY() - position().y());

        move($(movementX, movementY));
        return position();
    }

    @Override
    public double updateZoomRelative(final double delta) {
        return updateZoom(requestedZoom + delta);
    }

    @Override
    public Camera shake(CameraShakeOptions options) {
        start = Time.now();
        activeShake = options;
        return this;
    }

    @Override
    public Camera stopShaking() {
        activeShake = null;
        return this;
    }

    @Override
    public boolean isShaking() {
        return nonNull(activeShake);
    }

    @Override
    public double zoom() {
        return world.cameraZoom();
    }

    @Override
    public double minZoom() {
        return minZoom;
    }

    @Override
    public double maxZoom() {
        return maxZoom;
    }

    @Override
    public void update() {
        Time now = Time.now();

        if (activeShake != null) {
            shake = calculateDistortion(now);
            if (activeShake.duration().isNone() || now.isAfter(start.plus(activeShake.duration()))) {
                activeShake = null;
            }
        } else {
            shake = Vector.zero();
        }
    }

    private Vector calculateDistortion(Time now) {
        final Duration elapsed = Duration.between(start, now);
        final Time end = now.plus(activeShake.duration());
        final var progress = activeShake.duration().isNone()
                ? Percent.zero()
                : Percent.of(1.0 * elapsed.nanos() / Duration.between(start, end).nanos());
        return $(xNoise.value(now), yNoise.value(now)).multiply(activeShake.strength() * progress.invert().value() / zoom);
    }

}

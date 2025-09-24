package dev.screwbox.core.graphics;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Line;
import dev.screwbox.core.Angle;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.options.CircleDrawOptions;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.graphics.options.RectangleDrawOptions;
import dev.screwbox.core.utils.ListUtil;
import dev.screwbox.core.utils.Validate;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Can be added to a glow effect to create a more immersive light experience. Some configuration properties are relative
 * to the light source the effect is added to.
 *
 * @param orbs             orbs contained within the lens flare
 * @param rayCount         number of rays contained in the lens flare
 * @param rayRotationSpeed rotation speed relative to the x position of the glow effect on the screen
 * @param rayOpacity       ray opacity relative to the opacity of the glow effect
 * @param rayWidth         width of the light rays in pixels (does not scale with glow)
 * @param rayLength        ray length relative to the radius of the glow effect
 * @see LensFlareBundle
 * @see Light#addGlow(Vector, double, Color, LensFlare)
 * @since 3.8.0
 */
public record LensFlare(List<Orb> orbs, int rayCount, double rayRotationSpeed, double rayOpacity, int rayWidth,
                        double rayLength) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public LensFlare {
        Validate.zeroOrPositive(rayCount, "ray count must be positive");
        Validate.range(rayRotationSpeed, -10, 10, "ray rotation speed must be in range from -10 to 10");
        Validate.range(rayOpacity, 0.01, 10, "ray opacity must be in range from 0.01 to 10");
        Validate.range(rayWidth, 1, 64, "ray width must be in range from 1 to 64");
        Validate.range(rayLength, 0.1, 10, "ray length must be in range from 1 to 10");
    }

    private LensFlare(final List<Orb> orbs, final int rayCount) {
        this(orbs, rayCount, 0.1, 0.14, 1, 2.0);
    }

    /**
     * A single orb that will be placed on the extended line of the light source and the center of the {@link Screen}.
     *
     * @param distance distance relative to the distance between light source an center in the range of -10 to 10
     * @param size     size relative to radius of the light source in the range of 0.01 to 10
     * @param opacity  opacity relative to the opacity of the light source in the range of 0.01 to 10
     */
    public record Orb(double distance, double size, double opacity) implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        public Orb {
            Validate.range(distance, -10, 10, "orb distance must be in range -10 to 10");
            Validate.range(size, 0.01, 10, "orb size must be in range 0.01 to 10");
            Validate.range(opacity, 0.01, 10, "orb opacity must be in range 0.01 to 10");
        }
    }

    /**
     * Creates a new instance without rays.
     */
    public static LensFlare noRays() {
        return rayCount(0);
    }

    /**
     * Creates a new instance with the specified ray count.
     */
    public static LensFlare rayCount(int rayCount) {
        return new LensFlare(new ArrayList<>(), rayCount);
    }

    /**
     * Returns a new instance with an added light orb.
     */
    public LensFlare orb(final double distance, final double size, final double opacity) {
        final List<Orb> updatedOrbs = ListUtil.combine(orbs, new Orb(distance, size, opacity));
        return new LensFlare(updatedOrbs, rayCount, rayRotationSpeed, rayOpacity, rayWidth, rayLength);
    }

    /**
     * Returns a new instance with the specified ray rotation speed in the range of -10 to 10.
     */
    public LensFlare rayRotationSpeed(final double rayRotationSpeed) {
        return new LensFlare(orbs, rayCount, rayRotationSpeed, rayOpacity, rayWidth, rayLength);
    }

    /**
     * Returns a new instance with the specified ray opacity in the range of 0.01 to 10.
     */
    public LensFlare rayOpacity(final double rayOpacity) {
        return new LensFlare(orbs, rayCount, rayRotationSpeed, rayOpacity, rayWidth, rayLength);
    }

    /**
     * Returns a new instance with the specified ray width in pixels in the range of 1 to 64.
     */
    public LensFlare rayWidth(final int rayWidth) {
        return new LensFlare(orbs, rayCount, rayRotationSpeed, rayOpacity, rayWidth, rayLength);
    }

    /**
     * Returns a new instance with the specified ray length in the range of 0.1 to 10.
     */
    public LensFlare rayLength(final double rayLength) {
        return new LensFlare(orbs, rayCount, rayRotationSpeed, rayOpacity, rayWidth, rayLength);
    }

    /**
     * Renders the {@link LensFlare} for a point glow effect on the specified {@link Viewport}.
     */
    public void render(final Vector position, final double radius, final Color color, final Viewport viewport) {
        renderRays(position, radius, color, viewport);
        renderOrbs(position, radius, color, viewport);
    }

    /**
     * Renders the {@link LensFlare} for an rectangular glow effect on the specified {@link Viewport}.
     */
    public void render(final Bounds bounds, final double radius, final Color color, final Viewport viewport) {
        renderRays(bounds.position(), radius, color, viewport);
        renderOrbs(bounds, radius, color, viewport);
    }

    //TODO remove redundancy
    private void renderOrbs(final Bounds bounds, final double radius, final Color color, final Viewport viewport) {
        final var cameraPosition = viewport.camera().position();
        final var distanceToCamera = cameraPosition.substract(bounds.position());

        for (final var orb : orbs) {
            final var orbPosition = cameraPosition.add(distanceToCamera.multiply(orb.distance()));
            final double orbRadius = radius * orb.size();
            final var orbBounds = viewport.toCanvas(bounds.expand(orbRadius).moveTo(orbPosition));
            final var orbOptions = RectangleDrawOptions
                    .fading(color.opacity(color.opacity().value() * orb.opacity()))
                    .curveRadius((int)Math.min(orbBounds.width()/ 2.0, orbBounds.height() / 2.0));
            viewport.canvas().drawRectangle(orbBounds, orbOptions);
        }
    }

    private void renderOrbs(final Vector position, final double radius, final Color color, final Viewport viewport) {
        final var cameraPosition = viewport.camera().position();
        final var distanceToCamera = cameraPosition.substract(position);

        for (final var orb : orbs) {
            final var orbPosition = cameraPosition.add(distanceToCamera.multiply(orb.distance()));
            final double orbRadius = radius * orb.size();
            final var orbOptions = CircleDrawOptions.fading(color.opacity(color.opacity().value() * orb.opacity()));
            viewport.canvas().drawCircle(viewport.toCanvas(orbPosition), viewport.toCanvas(orbRadius), orbOptions);
        }
    }

    private void renderRays(final Vector position, final double radius, final Color color, final Viewport viewport) {
        final var fixedLine = Line.normal(position, rayLength * radius);
        final var options = LineDrawOptions
                .color(color.opacity(color.opacity().value() * rayOpacity))
                .strokeWidth(rayWidth);

        final Offset start = viewport.toCanvas(position);

        for (int rayNr = 0; rayNr < rayCount; rayNr++) {
            final var result = Angle.degrees(rayNr * 360.0 / rayCount + rayRotationSpeed * start.x()).applyOn(fixedLine);
            viewport.canvas().drawLine(start, viewport.toCanvas(result.to()), options);
        }
    }
}

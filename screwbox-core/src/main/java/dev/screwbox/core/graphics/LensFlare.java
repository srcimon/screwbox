package dev.screwbox.core.graphics;

import dev.screwbox.core.Line;
import dev.screwbox.core.Rotation;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.options.CircleDrawOptions;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.utils.Validate;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//TODO document and test
//TODO document in docusaurus
public record LensFlare(List<Orb> orbs, int rayCount, double rayRotationSpeed, double rayOpacity, int rayWidth,
                        double rayLength) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    //TODO LensFlareBundle

    private LensFlare(List<Orb> orbs, int rayCount) {
        this(orbs, rayCount, 0.1, 0.14, 1, 2.0);
    }

    public record Orb(double distance, double size, double opacity) implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        public Orb {
            Validate.range(distance, -10, 10, "distance must be in range -10 to 10");
            Validate.range(size, 0.01, 10, "size must be in range 0.01 to 10");
            Validate.range(opacity, 0.01, 10, "opacity must be in range 0.01 to 10");
        }
    }

    public static LensFlare noRays() {
        return rays(0);
    }

    public static LensFlare rays(int rayCount) {
        return new LensFlare(new ArrayList<>(), rayCount);
    }

    public LensFlare orb(final double distance, final double size, final double opacity) {
        final List<Orb> updatedOrbs = new ArrayList<>(orbs);
        updatedOrbs.add(new Orb(distance, size, opacity));
        return new LensFlare(updatedOrbs, rayCount);
    }

    public void render(final Vector position, final double radius, final Color color, final Viewport viewport) {
        if (viewport.visibleArea().contains(position)) {
            renderRays(position, radius, color, viewport);
            renderOrbs(radius, color, viewport, position);
        }
    }

    private void renderOrbs(double radius, Color color, Viewport viewport, Vector position) {
        final Vector cameraPosition = viewport.camera().position();
        final var positionToCamera = cameraPosition.substract(position);

        for (final var orb : orbs) {
            final var orbPosition = cameraPosition.add(positionToCamera.multiply(orb.distance()));
            final double orbRadius = radius * orb.size();
            final var orbOptions = CircleDrawOptions.fading(color.opacity(color.opacity().value() * orb.opacity()));
            viewport.canvas().drawCircle(viewport.toCanvas(orbPosition), viewport.toCanvas(orbRadius), orbOptions);
        }
    }

    private void renderRays(Vector position, double radius, Color color, Viewport viewport) {
        for (int i = 0; i < rayCount; i++) {
            var line = Line.normal(position, rayLength * radius);
            var result = Rotation.degrees(i * 360.0 / rayCount + rayRotationSpeed * viewport.toCanvas(position).x()).applyOn(line);
            LineDrawOptions options = LineDrawOptions.color(color.opacity(color.opacity().value() * rayOpacity)).strokeWidth(rayWidth);
            viewport.canvas().drawLine(viewport.toCanvas(result.from()), viewport.toCanvas(result.to()), options);
        }
    }
}

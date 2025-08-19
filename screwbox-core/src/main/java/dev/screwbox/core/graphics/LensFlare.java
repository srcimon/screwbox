package dev.screwbox.core.graphics;

import dev.screwbox.core.Line;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Rotation;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.options.CircleDrawOptions;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.utils.PerlinNoise;

import java.util.ArrayList;
import java.util.List;

//TODO document and test
//TODO document in docusaurus
//TODO avoid this to have no code at all
public class LensFlare {

    private record Orb(double distance, double size, double opacity) {

    }

    private final List<Orb> orbs = new ArrayList<>();

    public LensFlare addOrb(final double distance, final double size, final double opacity) {
        orbs.add(new Orb(distance, size, opacity));
        return this;
    }

    public void render(final Vector position, final double radius, final Color color, final Viewport viewport) {
        final Vector cameraPosition = viewport.camera().position();
        final var positionToCamera = cameraPosition.substract(position);
        for (final var orb : orbs) {
            final var orbPosition = cameraPosition.add(positionToCamera.multiply(orb.distance()));
            final double orbRadius = radius * orb.size();
            final var orbOptions = CircleDrawOptions.fading(color.opacity(color.opacity().value() * orb.opacity()));
            viewport.canvas().drawCircle(viewport.toCanvas(orbPosition), viewport.toCanvas(orbRadius), orbOptions);
        }

        //TODO extract ray config
        int rayCount = 2;
        double rayRelativeRotation = 0.1;
        double rayOpacity = 0.04;
        int rayWidthPixels = 3;
        double rayLength = 1.1;

        // rays
        for (int i = 0; i < rayCount; i++) {
            var line = Line.normal(position, rayLength * radius);
            var result = Rotation.degrees(i * 360.0 / rayCount + rayRelativeRotation * viewport.toCanvas(position).x()).applyOn(line);
            LineDrawOptions options = LineDrawOptions.color(color.opacity(color.opacity().value() * rayOpacity)).strokeWidth(rayWidthPixels);
            viewport.canvas().drawLine(viewport.toCanvas(result.from()), viewport.toCanvas(result.to()), options);
        }


        renderOrbs(position, radius, color, viewport);
    }

    private static void renderOrbs(Vector position, double radius, Color color, Viewport viewport) {
        int distance = 60;
        int spacing = 30;
        double zoom = 1000;
        double opacity = 0.08;
        double size = 0.1;
        int count = 5;

        // orbs
        for (double d = 0; d < 360; d += 360.0 / count) {
            var to = Rotation.degrees(d).applyOn(Line.normal(position, spacing)).to();
            Offset offset = viewport.toCanvas(to);
            double z = radius / 10.0 + color.rgb()/ 10.0 + position.x() / 100.0+ position.y() / 100.0;
            double z2 = radius / 3.0 + color.rgb()/ 8.0+ position.x() / 100.0+ position.y() / 100.0;

            var noise = Percent.of((PerlinNoise.generatePerlinNoise3d((int)d + 1938891L, offset.x() / zoom, offset.y() / zoom, z) + 1) / 2.0);
            var noise2 = Percent.of((PerlinNoise.generatePerlinNoise3d((int)d + 1485952L, offset.x() / zoom, offset.y() / zoom, z2) + 1) / 2.0);
            Offset dotOffset = viewport.toCanvas(to.add(noise.rangeValue(-distance, distance), noise2.rangeValue(-distance, distance)));

            viewport.canvas().drawCircle(dotOffset, (int) (viewport.toCanvas(radius) * size), CircleDrawOptions.filled(color.opacity(color.opacity().value() * opacity * noise.value())));
        }
    }
}

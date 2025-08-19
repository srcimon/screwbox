package dev.screwbox.core.graphics;

import dev.screwbox.core.Line;
import dev.screwbox.core.Rotation;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.options.CircleDrawOptions;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.utils.FractalNoise;

import java.util.ArrayList;
import java.util.List;

//TODO document and test
//TODO document in docusaurus
//TODO avoid this to have no code at all
public class LensFlare {

    private record Halo(double distance, double size, double opacity) {

    }

    private List<Halo> halos = new ArrayList<>();

    public LensFlare addHalo(final double distance, final double size, final double opacity) {
        halos.add(new Halo(distance, size, opacity));
        return this;
    }

    public void render(final Vector position, final double radius, final Color color, final Viewport viewport) {
        final Vector cameraPosition = viewport.camera().position();
        final var positionToCamera = cameraPosition.substract(position);
        for (final var orb : halos) {
            final var orbPosition = cameraPosition.add(positionToCamera.multiply(orb.distance()));
            final double orbRadius = radius * orb.size();
            final var orbOptions = CircleDrawOptions.fading(color.opacity(color.opacity().value() * orb.opacity()));
            viewport.canvas().drawCircle(viewport.toCanvas(orbPosition), viewport.toCanvas(orbRadius), orbOptions);
        }

        //TODO extract ray config
        int numberOfRays = 4;
        double rayRotationSpeed = 0.1;
        double rayOpacity = 0.03;
        int rayWidth = 3;
        double rayLength = 1.5;

        // rays
        for (int i = 0; i < numberOfRays; i++) {
            var line = Line.normal(position, rayLength * radius);
            var result = Rotation.degrees(i * 360.0 / numberOfRays + rayRotationSpeed * viewport.toCanvas(position).x()).applyOn(line);
            viewport.canvas().drawLine(viewport.toCanvas(position), viewport.toCanvas(result.to()), LineDrawOptions.color(color.opacity(color.opacity().value() * rayOpacity)).strokeWidth(rayWidth));
        }

        int distance = 100;
        int spacing = 40;
        int zoom = 2500;
        double opacity = 0.07;
        double size = 0.1;

        // orbs
        for (int i = -distance; i < distance; i += spacing) {
            for (int y = -distance; y < distance; y += spacing) {
                Offset offset = viewport.toCanvas(position.addX(i).addY(y));
                var noise = FractalNoise.generateFractalNoise(zoom, 12394L, offset);
                var noise2 = FractalNoise.generateFractalNoise(zoom, 12342344L, offset);
                Offset dotOffset = offset.add(
                        noise.rangeValue(-distance*2, distance*2),
                        noise2.rangeValue(-distance*2, distance*2)
                );
                var dotdistance = viewport.toCanvas(position).distanceTo(dotOffset);

                var modifier = Math.max(0, distance * 2 - dotdistance) / dotdistance * opacity;

                viewport.canvas().drawCircle(dotOffset, (int) (viewport.toCanvas(radius) * size), CircleDrawOptions.filled(color.opacity(color.opacity().value() * modifier)));
            }
        }
    }
}

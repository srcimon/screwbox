package dev.screwbox.core.graphics;

import dev.screwbox.core.Line;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Rotation;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.options.CircleDrawOptions;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.utils.FractalNoise;
import dev.screwbox.core.utils.PerlinNoise;

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

        int distance = 200;
        int spacing = 40;
        double zoom = 4000;
        double opacity = 0.07;
        double size = 0.1;
int count = 12;

        // orbs
        for(double d = 0; d < 360; d+= 360.0/count) {
            var to = Rotation.degrees(d).applyOn(Line.normal(position, spacing)).to();
            Offset offset = viewport.toCanvas(to);
            var noise = Percent.of((PerlinNoise.generatePerlinNoise(12394L, d+offset.x() / zoom, d+offset.y() / zoom) +1) / 2.0);
            var noise2 = Percent.of((PerlinNoise.generatePerlinNoise(534345L, d+offset.x() / zoom, d+offset.y() / zoom) +1) / 2.0);
            Offset dotOffset = viewport.toCanvas(to.add(noise.rangeValue(-distance, distance), noise2.rangeValue(-distance, distance)));

            viewport.canvas().drawCircle(dotOffset, (int) (viewport.toCanvas(radius) * size), CircleDrawOptions.filled(color.opacity(color.opacity().value() * opacity)));
        }
    }
}

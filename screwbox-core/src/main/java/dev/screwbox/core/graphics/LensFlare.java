package dev.screwbox.core.graphics;

import dev.screwbox.core.Line;
import dev.screwbox.core.Rotation;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.options.CircleDrawOptions;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.utils.FractalNoise;

import java.util.ArrayList;
import java.util.List;

import static dev.screwbox.core.graphics.Color.WHITE;

//TODO document and test
//TODO document in docusaurus
//TODO avoid this to have no code at all
public class LensFlare {

    private record Orb(double distance, double size, double opacity) {

    }

    private List<Orb> orbs = new ArrayList<>();

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
        int numberOfRays = 4;
        double rayRotationSpeed = 0.1;
        double rayOpacity = 0.03;
        int rayWidth = 3;
        double rayLength = 1.5;

        for (int i = 0; i < numberOfRays; i++) {
            var line = Line.normal(position, rayLength * radius);
            var result = Rotation.degrees(i * 360.0 / numberOfRays + rayRotationSpeed * viewport.toCanvas(position).x()).applyOn(line);
            viewport.canvas().drawLine(viewport.toCanvas(position), viewport.toCanvas(result.to()), LineDrawOptions.color(color.opacity(color.opacity().value() * rayOpacity)).strokeWidth(rayWidth));
        }

        for (int i = -60; i < 60; i += 20) {
            for (int y = -60; y < 60; y += 20) {
                Offset offset = viewport.toCanvas(position.addX(i).addY(y));
                var noise = FractalNoise.generateFractalNoise(800, 12394L, offset);
                var noise2 = FractalNoise.generateFractalNoise(800, 12342344L, offset);
                viewport.canvas().drawCircle(offset.add(
                        noise.rangeValue(-100, 100),
                        noise2.rangeValue(-400, 400)
                ), 20, CircleDrawOptions.filled(color.opacity(color.opacity().value() * 0.03)));
            }
        }
    }
}

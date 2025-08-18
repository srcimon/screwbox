package dev.screwbox.core.graphics;

import dev.screwbox.core.Line;
import dev.screwbox.core.Rotation;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.options.CircleDrawOptions;
import dev.screwbox.core.graphics.options.LineDrawOptions;

import java.util.ArrayList;
import java.util.List;

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
        double rayRotationSpeed = 0.5;
        double rayOpacity = 0.01;
        int rayWidth = 4;

        for (int i = 0; i < numberOfRays; i++) {
            var line = Line.normal(position, 100);
            var result = Rotation.degrees(i * 360.0 / numberOfRays + rayRotationSpeed * viewport.toCanvas(position).x()).applyOn(line);
            viewport.canvas().drawLine(viewport.toCanvas(position), viewport.toCanvas(result.to()), LineDrawOptions.color(color.opacity(color.opacity().value() * rayOpacity)).strokeWidth(rayWidth));
        }
    }
}

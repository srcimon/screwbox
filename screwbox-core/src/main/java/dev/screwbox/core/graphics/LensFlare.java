package dev.screwbox.core.graphics;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.options.CircleDrawOptions;

import java.util.ArrayList;
import java.util.List;

//TODO document and test
//TODO document in docusaurus
public class LensFlare {

    private static final double MINIMAL_VISIBLE_GRADIENT = 0.01;


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
            final Bounds orbLightBox = Bounds.atPosition(orbPosition, orbRadius * 2, orbRadius * 2);
            final double orbOpacity = color.opacity().value() * orb.opacity();
//TODO visibility check not necessary because its in firewallrenderer
            //TODO add MINIMAL VISIBLE GRADIENT to firewall renderer
            if (orbOpacity > MINIMAL_VISIBLE_GRADIENT && viewport.canvas().isVisible(viewport.toCanvas(orbLightBox))) {
                final var orbOptions = CircleDrawOptions.fading(color.opacity(orbOpacity));
                viewport.canvas().drawCircle(viewport.toCanvas(orbPosition), viewport.toCanvas(orbRadius), orbOptions);
            }
        }
    }
}

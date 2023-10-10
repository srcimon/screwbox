package io.github.srcimon.screwbox.examples.animation.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.entities.Archetype;
import io.github.srcimon.screwbox.core.entities.EntitySystem;
import io.github.srcimon.screwbox.core.entities.components.TransformComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.World;

import java.util.*;

public class RenderAnimationSystem implements EntitySystem {

    private static final Archetype DOTS = Archetype.of(TransformComponent.class);

    private record Triangle(List<Vector> dots) {
    }
    @Override
    public void update(final Engine engine) {
        List<Vector> dotPositions = new ArrayList<>(engine.entities().fetchAll(DOTS).stream()
                .map(dot -> dot.get(TransformComponent.class).bounds.position())
                .toList());


        // every dot creates triangle with nearest two dots
        // but not if there is already a triangle with these dots

        World graphicsWorld = engine.graphics().world();
        for(final var position : dotPositions) {
            graphicsWorld.fillCircle(position, 8, Color.WHITE);
        }
List<Triangle> allTriangles = new ArrayList<>();

        for(final var position : dotPositions) {
            var triangle = findTriangle(position, dotPositions);
            if(triangle.isPresent() && !allTriangles.contains(triangle.get())) {
                allTriangles.add(triangle.get());
            }
        }

        for(final var triangle : allTriangles) {
            graphicsWorld
                    .drawLine(triangle.dots.get(0), triangle.dots.get(1), Color.WHITE)
            .drawLine(triangle.dots.get(1), triangle.dots.get(2), Color.WHITE)
            .drawLine(triangle.dots.get(2), triangle.dots.get(0), Color.WHITE);
        }

    }

    private Optional<Triangle> findTriangle(Vector start, List<Vector> positions) {
        if(positions.size() < 3) {
            return Optional.empty();
        }
        List<Vector> byDistance = new ArrayList<>();
        byDistance.addAll(positions);

        byDistance.sort(new Comparator<Vector>() {
            @Override
            public int compare(Vector me, Vector other) {
                return Double.compare(me.distanceTo(start), other.distanceTo(start));
            }
        });

        return Optional.of(new Triangle(List.of(byDistance.get(0), byDistance.get(1), byDistance.get(2))));
    }

}

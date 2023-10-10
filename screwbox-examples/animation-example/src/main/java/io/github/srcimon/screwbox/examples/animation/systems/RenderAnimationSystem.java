package io.github.srcimon.screwbox.examples.animation.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Segment;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.entities.Archetype;
import io.github.srcimon.screwbox.core.entities.EntitySystem;
import io.github.srcimon.screwbox.core.entities.components.TransformComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.World;
import io.github.srcimon.screwbox.core.utils.MathUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class RenderAnimationSystem implements EntitySystem {

    private static final Archetype DOTS = Archetype.of(TransformComponent.class);

    private record Triangle(List<Vector> dots) {

        public List<Segment> segments() {
            return List.of(
                    Segment.between(dots.get(0), dots.get(1)),
                    Segment.between(dots.get(1), dots.get(2)),
                    Segment.between(dots.get(2), dots.get(0))
            );
        }
    }

    @Override
    public void update(final Engine engine) {
        List<Vector> dotPositions = new ArrayList<>(engine.entities().fetchAll(DOTS).stream()
                .map(dot -> dot.get(TransformComponent.class).bounds.position())
                .toList());


        // every dot creates triangle with nearest two dots
        // but not if there is already a triangle with these dots

        World graphicsWorld = engine.graphics().world();
        for (final var position : dotPositions) {
            graphicsWorld.fillCircle(position, 8, Color.WHITE);
        }
        List<Triangle> allTriangles = new ArrayList<>();
        List<Segment> allSegments = new ArrayList<>();

        for (final var position : dotPositions) {
            var triangle = findNextTriangle(position, dotPositions, allSegments);
            if (triangle.isPresent() && !allTriangles.contains(triangle.get())) {
                allTriangles.add(triangle.get());
                allSegments.addAll(triangle.get().segments());
            }
        }

        for (final var triangle : allTriangles) {
            for (final var segment : triangle.segments()) {
                graphicsWorld.drawLine(segment, Color.WHITE);
            }
        }

    }

    private Optional<Triangle> findNextTriangle(Vector start, List<Vector> positions, List<Segment> blockedSegments) {
        if (positions.size() < 3) {
            return Optional.empty();
        }
        List<Vector> byDistance = new ArrayList<>();
        byDistance.addAll(positions);
        byDistance.remove(start);

        byDistance.sort(Comparator.comparingDouble(vector -> vector.distanceTo(start)));

        Optional<Vector> nearesUnblocked = findNearestUnblocked(start, byDistance, blockedSegments);
        if(nearesUnblocked.isEmpty()) {
            return Optional.empty();
        }
        byDistance.remove(nearesUnblocked.get());

        Optional<Vector> secondNearestUnblocked = findNearestUnblocked(start, byDistance, blockedSegments);
        if(secondNearestUnblocked.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new Triangle(List.of(start, nearesUnblocked.get(), secondNearestUnblocked.get())));
    }

    private Optional<Vector> findNearestUnblocked(Vector me, List<Vector> byDistance, List<Segment> blockedSegments) {
        for(var nearest : byDistance) {
            Segment seg = Segment.between(me, nearest
                    .addX(MathUtil.modifier(nearest.x() +me.x()) * 0.00001)
                    .addY(MathUtil.modifier(nearest.y() +me.y()) * 0.00001)

            );
            boolean isFree = blockedSegments.stream().noneMatch(s -> s.intersects(seg));
            if(isFree) {
                return Optional.of(nearest);
            }
        }
        return Optional.empty();
    }

}

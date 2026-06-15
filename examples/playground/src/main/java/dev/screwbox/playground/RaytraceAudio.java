package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Line;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.Vector;

import java.util.ArrayList;
import java.util.List;

public class RaytraceAudio {

    record RayHit(String name, Polygon polygon, double length, double wallCount) {

    }

    public List<RayHit> findAllRayHits(Vector source, Vector target, List<Bounds> walls, double maxLength) {
        List<RayHit> hits = new ArrayList<>();
        var direct = Line.between(source, target);
        if (direct.length() >= maxLength) {
            return hits;
        }
        var colliding = fetchCollidingBounds(direct, walls);
        if (colliding.isEmpty()) {
            hits.add(new RayHit("direct", Polygon.ofNodes(source, target), direct.length(), 0));
        }
        if (colliding.size() == 1) {
            var expanded = colliding.getFirst().expand(0.5);
            for (var corner : expanded.corners()) {
                var collidingInner = fetchCollidingBounds(Line.between(corner, target), walls);
                if (collidingInner.isEmpty()) {
                    Polygon polygon = Polygon.ofNodes(source, corner, target);
                    hits.add(new RayHit("reflection", polygon, polygon.length(), 0));
                }
            }
        }
        return hits;
    }

    private static List<Bounds> fetchCollidingBounds(Line line, List<Bounds> walls) {
        final List<Bounds> colliding = new ArrayList<>();
        for (final var wall : walls) {
            for (var border : wall.borders()) {
                if (border.intersects(line)) {
                    colliding.add(wall);
                }
            }
        }
        return colliding;
    }
}

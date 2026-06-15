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
        if (direct.length() <= maxLength && isFree(direct, walls)) {
            hits.add(new RayHit("direct", Polygon.ofNodes(source, target), direct.length(), 0));
        }
        return hits;
    }

    private static boolean isFree(Line line, List<Bounds> walls) {
        for (final var wall : walls) {
            for (var border : wall.borders()) {
                if (border.intersects(line)) {
                    return false;
                }
            }
        }
        return true;
    }
}

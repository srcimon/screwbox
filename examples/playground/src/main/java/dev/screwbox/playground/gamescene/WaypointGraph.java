package dev.screwbox.playground.gamescene;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Line;
import dev.screwbox.core.Vector;
import dev.screwbox.core.navigation.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO may be a new feature
public class WaypointGraph implements Graph<Vector> {

    private Map<Vector, List<Vector>> waypoints = new HashMap<>();

    public WaypointGraph(List<Vector> waypoints, List<Bounds> obstacles) {//TODO obstacle extrusions
        var colliderLines = obstacles.stream().flatMap(o -> o.borders().stream()).toList();
        for(var waypoint : waypoints) {
            this.waypoints.put(waypoint, new ArrayList<>());
        }
        for(var waypoint : waypoints) {
            var list = this.waypoints.get(waypoint);
            for(var other : waypoints) {
                var connection = Line.between(waypoint, other);
                if(!waypoint.equals(other)) {
                    if(isFree(connection, colliderLines)) {
                            list.add(other);
                    }
                }
            }
        }
    }

    private boolean isFree(Line connection, List<Line> colliderLines) {
        for(final var line : colliderLines) {
            if(line.intersects(connection)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Vector> adjacentNodes(Vector node) {
        return waypoints.get(node);
    }

    @Override
    public double traversalCost(Vector start, Vector end) {
        return 1;
    }

    @Override
    public Vector toGraph(Vector position) {
        for(var waypoint : waypoints.keySet()) {
            if(waypoint.distanceTo(position) < 100) {
                return waypoint;
            }
        }
        return null;
    }

    @Override
    public Vector toPosition(Vector node) {
        return node;
    }

    @Override
    public boolean nodeExists(Vector node) {
        return waypoints.containsKey(node);
    }
}

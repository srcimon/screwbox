package de.suzufa.screwbox.examples.pathfinding.EXPERIMENTAL;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.suzufa.screwbox.core.Segment;
import de.suzufa.screwbox.core.Vector;

public class Path implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Vector> waypoints;

    private Path(List<Vector> waypoints) {
        this.waypoints = waypoints;
    }

    public static Path withWaypoints(List<Vector> waypoints) {
        // TODO: check not empty
        return new Path(waypoints);
    }

    public Vector nextWaypoint(Vector position) {
        List<Vector> all = new ArrayList<>(waypoints);
        Collections.sort(all, new Comparator<Vector>() {

            @Override
            public int compare(Vector o1, Vector o2) {
                return Double.compare(position.distanceTo(o1), position.distanceTo(o2));// TODO: duplicate code?
            }
        });
        return all.get(0);
    }

    public List<Segment> segments() {
        var segments = new ArrayList<Segment>();
        for (int i = 0; i < nodeCount() - 1; i++) {
            segments.add(Segment.between(waypoints.get(i), waypoints.get(i + 1)));
        }
        return segments;
    }

    public List<Vector> waypoints() {
        return waypoints;
    }

    public Vector start() {
        if (waypoints.isEmpty()) {
            throw new IllegalStateException("Path has no waypoints.");
        }
        return waypoints.get(0);
    }

    public int nodeCount() {
        return waypoints.size();
    }

    public Vector end() {
        return waypoints.get(nodeCount() - 1);
    }
}

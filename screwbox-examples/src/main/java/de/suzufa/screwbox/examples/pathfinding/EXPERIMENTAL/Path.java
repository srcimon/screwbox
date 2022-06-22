package de.suzufa.screwbox.examples.pathfinding.EXPERIMENTAL;

import java.io.Serializable;
import java.util.ArrayList;
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

    public boolean hasNodes() {
        return !waypoints.isEmpty();
    }

    public Vector start() {
        if (waypoints.isEmpty()) {
            throw new IllegalStateException("Path has no waypoints.");
        }
        return waypoints.get(0);
    }

    public void nextNode() {
        // TODO: CheckNotEMpty;
        waypoints.remove(0);
    }

    public int nodeCount() {
        return waypoints.size();
    }
}

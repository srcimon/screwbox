package de.suzufa.screwbox.examples.pathfinding.EXPERIMENTAL;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.Segment;
import de.suzufa.screwbox.core.Vector;

public class Path implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Vector> nodes;

    private Path(List<Vector> nodes) {
        this.nodes = nodes;
    }

    public static Path withNodes(List<Vector> nodes) {
        // TODO: check not empty
        return new Path(nodes);
    }

    public List<Segment> segments() {
        var segments = new ArrayList<Segment>();
        for (int i = 0; i < nodeCount() - 1; i++) {
            segments.add(Segment.between(nodes.get(i), nodes.get(i + 1)));
        }
        return segments;
    }

    public void dropFirstNode() {
        if (nodeCount() == 1) {
            throw new IllegalStateException("Cannot drop last node.");
        }
        nodes.remove(0);
    }

    public List<Vector> nodes() {
        return nodes;
    }

    public Vector start() {
        return nodes.get(0);
    }

    public int nodeCount() {
        return nodes.size();
    }

    public Vector end() {
        return nodes.get(nodeCount() - 1);
    }
}

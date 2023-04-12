package io.github.srcimon.screwbox.core;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Path implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<Vector> nodes;

    public static Path withNodes(final List<Vector> nodes) {
        return new Path(nodes);
    }

    private Path(final List<Vector> nodes) {
        if (nodes.isEmpty()) {
            throw new IllegalArgumentException("Path must have at least one node.");
        }
        this.nodes = nodes;
    }

    public List<Segment> segments() {
        final var segments = new ArrayList<Segment>();
        for (int i = 0; i < nodeCount() - 1; i++) {
            final var segment = Segment.between(nodes.get(i), nodes.get(i + 1));
            segments.add(segment);
        }
        return segments;
    }

    public void removeNode(final int node) {
        if (nodeCount() == 1) {
            throw new IllegalStateException("Cannot drop last node.");
        }
        if (node > nodes.size()) {
            throw new IllegalStateException("Path doesnt have node: " + node);
        }
        nodes.remove(node);
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

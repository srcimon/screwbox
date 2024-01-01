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
            throw new IllegalArgumentException("path must have at least one node");
        }
        this.nodes = nodes;
    }

    public List<Line> segments() {
        final var segments = new ArrayList<Line>();
        for (int i = 0; i < nodeCount() - 1; i++) {
            final var segment = Line.between(nodes.get(i), nodes.get(i + 1));
            segments.add(segment);
        }
        return segments;
    }

    public Path removeNode(final int node) {
        if (nodeCount() == 1) {
            throw new IllegalStateException("can not drop last node");
        }
        if (node > nodes.size()) {
            throw new IllegalStateException("path too short");
        }

        return Path.withNodes(nodes.subList(1, nodes.size()));
    }

    public List<Vector> nodes() {
        return nodes;
    }

    public Vector firstNode() {
        return nodes.getFirst();
    }

    public int nodeCount() {
        return nodes.size();
    }

    public Vector lastNode() {
        return nodes.get(nodeCount() - 1);
    }
}

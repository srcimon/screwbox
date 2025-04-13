package io.github.srcimon.screwbox.core;

import io.github.srcimon.screwbox.core.utils.Validate;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.unmodifiableList;

public class Path implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<Vector> nodes;
    private final List<Line> segments = new ArrayList<>();

    public static Path withNodes(final List<Vector> nodes) {
        return new Path(nodes);
    }

    private Path(final List<Vector> nodes) {
        Validate.notEmpty(nodes, "path must have at least one node");
        this.nodes = nodes;
    }

    public List<Line> segments() {
        if(segments.isEmpty()) {
            for (int i = 0; i < nodeCount() - 1; i++) {
                final var segment = Line.between(nodes.get(i), nodes.get(i + 1));
                segments.add(segment);
            }
        }
        return unmodifiableList(segments);
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
        return Collections.unmodifiableList(nodes);
    }

    public Vector firstNode() {
        return nodes.getFirst();
    }

    public int nodeCount() {
        return nodes.size();
    }

    public Vector lastNode() {
        return nodes.getLast();
    }
}

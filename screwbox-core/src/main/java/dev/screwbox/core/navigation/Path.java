package dev.screwbox.core.navigation;

import dev.screwbox.core.Line;
import dev.screwbox.core.Vector;
import dev.screwbox.core.utils.Validate;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.isNull;

/**
 * Represents a list of {@link Vector nodes} within the game world.
 */
public class Path implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<Vector> nodes;
    private List<Line> segments;

    /**
     * Create a new instance from the specified nodes. Needs at least one node.
     */
    public static Path withNodes(final List<Vector> nodes) {
        return new Path(nodes);
    }

    private Path(final List<Vector> nodes) {
        Validate.notEmpty(nodes, "path must have at least one node");
        this.nodes = unmodifiableList(nodes);
    }

    /**
     * Returns the segments between the nodes.
     */
    public List<Line> segments() {
        if (isNull(segments)) {
            initializeSegments();
        }
        return segments;
    }

    private void initializeSegments() {
        final var segmentsValue = new ArrayList<Line>();
        for (int i = 0; i < nodeCount() - 1; i++) {
            final var segment = Line.between(nodes.get(i), nodes.get(i + 1));
            segmentsValue.add(segment);
        }
        segments = unmodifiableList(segmentsValue);
    }

    /**
     * Removes the node with the specified number.
     */
    public Path removeNode(final int node) {
        if (nodeCount() == 1) {
            throw new IllegalStateException("can not drop last node");
        }
        if (node > nodes.size()) {
            throw new IllegalStateException("path too short");
        }

        return Path.withNodes(nodes.subList(1, nodes.size()));
    }

    /**
     * Returns the path {@link Vector nodes}.
     */
    public List<Vector> nodes() {
        return nodes;
    }

    /**
     * Returns the specified {@link Vector node}.
     *
     * @since 2.20.0
     */
    public Vector node(int number) {
        return nodes.get(number);
    }

    /**
     * Returns the start {@link Vector node}.
     */
    public Vector start() {
        return nodes.getFirst();
    }

    /**
     * Returns the {@link Vector node} count.
     */
    public int nodeCount() {
        return nodes.size();
    }

    /**
     * Returns the end {@link Vector node}.
     */
    public Vector end() {
        return nodes.getLast();
    }
}

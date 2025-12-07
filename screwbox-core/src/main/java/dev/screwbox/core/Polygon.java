package dev.screwbox.core;

import dev.screwbox.core.utils.ListUtil;
import dev.screwbox.core.utils.Validate;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static dev.screwbox.core.Vector.$;
import static dev.screwbox.core.utils.MathUtil.isUneven;
import static java.util.Collections.unmodifiableList;
import static java.util.Objects.isNull;

/**
 * A polygon shape within the game world made of a list of {@link Vector nodes}. Can be closed or open.
 */
public class Polygon implements Serializable {

    private static final Vector POINT_OUTSIDE_POLYGON = $(Double.MAX_VALUE / 1_000_000_099, Double.MAX_VALUE / 1_000_023);

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<Vector> nodes;
    private List<Line> segments;

    /**
     * Create a new instance from the specified nodes. Needs at least one node.
     */
    public static Polygon ofNodes(final List<Vector> nodes) {
        return new Polygon(nodes);
    }

    private Polygon(final List<Vector> nodes) {
        Validate.notEmpty(nodes, "polygon must have at least one node");
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

    /**
     * Returns a new instance without the node with the specified position within the polygon.
     */
    public Polygon removeNode(final int node) {
        if (nodeCount() == 1) {
            throw new IllegalStateException("polygon must have at least one node");
        }
        if (node > nodes.size()) {
            throw new IllegalStateException("polygon does not contain node " + node);
        }

        return Polygon.ofNodes(nodes.subList(1, nodes.size()));
    }

    /**
     * Returns the closest point on the {@link Polygon} to the specified point.
     *
     * @since 3.17.0
     */
    public Vector closestPoint(final Vector point) {
        Vector closest = firstNode();
        double closestDistance = closest.distanceTo(point);
        for (final var segment : segments()) {
            final var closestOnSegment = segment.closestPoint(point);
            if (closestOnSegment.distanceTo(point) < closestDistance) {
                closest = closestOnSegment;
                closestDistance = closestOnSegment.distanceTo(point);
            }
        }
        return closest;
    }

    /**
     * Returns a new instance with an additional node.
     *
     * @since 3.17.0
     */
    public Polygon addNode(final Vector node) {
        Objects.requireNonNull(node, "node must not be null");
        return Polygon.ofNodes(ListUtil.combine(nodes, node));
    }

    /**
     * Returns all {@link Vector nodes}.
     */
    public List<Vector> nodes() {
        return nodes;
    }

    /**
     * Returns the start {@link Vector node}.
     */
    public Vector firstNode() {
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
    public Vector lastNode() {
        return nodes.getLast();
    }

    @Override
    public String toString() {
        return "Polygon[nodes=" + nodes + ']';
    }

    /**
     * Returns {@code true} if the polygon is closed.
     *
     * @since 3.17.0
     */
    public boolean isClosed() {
        return firstNode().equals(lastNode());
    }

    //TODO changelog
    //TODO document
    public boolean contains(final Vector position) {
        if (isOpen()) {
            return false;
        }
        final var testLine = Line.between(position, POINT_OUTSIDE_POLYGON);
        int intersectionCount = 0;
        for (final var segment : segments()) {
            if (segment.intersects(testLine)) {
                intersectionCount++;
            }
        }
        return isUneven(intersectionCount);
    }

    private void initializeSegments() {
        final var segmentsValue = new ArrayList<Line>();
        for (int i = 0; i < nodeCount() - 1; i++) {
            final var segment = Line.between(nodes.get(i), nodes.get(i + 1));
            segmentsValue.add(segment);
        }
        segments = unmodifiableList(segmentsValue);
    }

    public Line bisectorRayOfNode(final int nodeNr) {
        final Vector previousNode = nodeNr == 0 && isClosed()
                ? node(nodeCount() - 2)
                : previousNode(nodeNr);
        final Vector node = node(nodeNr);
        final Vector nextNode = nodeNr == (nodeCount()-1) && isClosed()
                ? node(1)
                : nextNode(nodeNr);

        var left = Line.between(node, previousNode);
        var right = Line.between(node, nextNode);

        return Angle.between(left, right).applyOn(Line.normal(node, -20));
    }


    /**
     * Returns the specified {@link Vector node}.
     *
     * @since 2.20.0
     */
    public Vector node(int number) {
        //TODO document
        Validate.range(number, 0, nodeCount() - 1, "node number not in valid range");
        return nodes.get(number);
    }

    //TODO test and document + changelog
    public Vector nextNode(final int nodeNr) {
        final int index = nodeNr == nodeCount() - 1 && isClosed()
                ? 0
                : nodeNr + 1;
        return node(index);
    }

    //TODO test and document + changelog
    public Vector previousNode(final int nodeNr) {
        final int index = nodeNr == 0 && isClosed()
                ? nodeCount() - 1
                : nodeNr - 1;
        return node(index);
    }

    public boolean isOpen() {
        return !isClosed();
    }
}

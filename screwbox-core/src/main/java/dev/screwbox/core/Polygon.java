package dev.screwbox.core;

import dev.screwbox.core.utils.LazyValue;
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

/**
 * A polygon shape within the game world made of a list of {@link Vector nodes}. Can be closed or open.
 *
 * @since 3.17.0
 */
public final class Polygon implements Serializable {

    // Some point that is considered never to reside within a polygon
    private static final Vector POINT_OUTSIDE_POLYGON = $(Double.MAX_VALUE / 1_000_000_099.123, Double.MAX_VALUE / 1_000_023.456);

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<Vector> definitionNodes;
    private final LazyValue<List<Vector>> nodes;
    private final LazyValue<List<Line>> segments;
    private final LazyValue<Vector> center;

    /**
     * Create a new instance from the specified nodes. Needs at least one node.
     */
    public static Polygon ofNodes(final List<Vector> nodes) {
        return new Polygon(nodes);
    }

    private Polygon(final List<Vector> nodes) {
        Validate.notEmpty(nodes, "polygon must have at least one node");
        this.definitionNodes = unmodifiableList(nodes);
        this.nodes = new LazyValue<>(this::initializeNodes);
        this.segments = new LazyValue<>(this::initializeSegments);
        this.center = new LazyValue<>(this::initializeCenter);
    }

    /**
     * Returns the segments between the nodes.
     */
    public List<Line> segments() {
        return segments.value();
    }

    /**
     * Returns a new instance without the node with the specified position within the polygon.
     */
    public Polygon removeNode(final int node) {
        if (definitionNodes.size() == 1) {
            throw new IllegalStateException("polygon must have at least one node");
        }
        if (node > definitionNodes.size()) {
            throw new IllegalStateException("polygon does not contain node " + node);
        }

        return Polygon.ofNodes(definitionNodes.subList(1, definitionNodes.size()));
    }

    /**
     * Returns the closest point on the {@link Polygon} to the specified point.
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
     */
    public Polygon addNode(final Vector node) {
        Objects.requireNonNull(node, "node must not be null");
        return Polygon.ofNodes(ListUtil.combine(definitionNodes, node));
    }

    /**
     * Returns all {@link Vector nodes} from the polygon definition. Will return the {@link #lastNode()} even if it's
     * the same as the {@link #firstNode()}.
     *
     * @see #nodes()
     */
    public List<Vector> definitionNotes() {
        return definitionNodes;
    }

    /**
     * Returns the nodes of the {@link Polygon}. Will not return {@link #lastNode()} when the polygon {@link #isClosed()}.
     */
    public List<Vector> nodes() {
        return nodes.value();
    }

    /**
     * Returns the start {@link Vector node}.
     */
    public Vector firstNode() {
        return definitionNodes.getFirst();
    }

    /**
     * Returns the {@link Vector node} count. Counts first and last node as one when {@link #isClosed()}.
     */
    public int nodeCount() {
        return nodes().size();
    }

    /**
     * Returns the end {@link Vector node}.
     */
    public Vector lastNode() {
        return definitionNodes.getLast();
    }

    @Override
    public String toString() {
        return "Polygon[nodes=" + definitionNodes + ']';
    }

    /**
     * Returns {@code true} if the polygon is closed.
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

    /**
     * Returns the center of the {@link Polygon}.
     */
    public Vector center() {
        return center.value();
    }

    /**
     * Returns {@code true} if the {@link Polygon} nodes are oriented clockwise.
     * Will be false if {@link #isClosed()} or has less than three nodes.
     *
     * @see <a href="https://en.wikipedia.org/wiki/Shoelace_formula">Shoelace formula</a>
     */
    public boolean isOrientedClockwise() {
        if (isOpen() || nodeCount() < 3) {
            return false;
        }
        double sum = 0;
        for (final var segment : segments()) {
            sum += segment.start().x() * segment.end().y() - segment.end().x() * segment.start().y();
        }
        return sum >= 0;
    }

    public Line bisectorRayOfNode(final int nodeNr) {
        final Vector previousNode = previousNode(nodeNr);
        final Vector node = node(nodeNr);
        final Vector nextNode = nextNode(nodeNr);
        final var angle = Angle.betweenLines(node, previousNode, nextNode);

        //TODO FIX first intersection!
        //TODO fix wrapped to outside
        //TODO fix tutorial says half line not full (not sure why)
        Line ray = Angle.of(Line.between(node, nextNode)).addDegrees(angle.degrees() / 2.0).applyOn(Line.normal(node, 10000));//TODO Calc?
        if (isOrientedClockwise()) {
            ray = Angle.degrees(180).applyOn(ray);//TODO remove workaround below
        }
        final List<Line> segments = segments();
        for (final var segment : segments) {
            if (!segment.start().equals(ray.start()) && !segment.end().equals(ray.start())) {
                Vector intersectPoint = ray.intersectionPoint(segment);
                if (intersectPoint != null) {
                    return Line.between(ray.start(), intersectPoint);
                }
            }
        }
        return Line.between(ray.start(), ray.start());
    }


    /**
     * Returns the specified {@link Vector node}.
     *
     * @since 2.20.0
     */
    public Vector node(int number) {
        //TODO document
        Validate.range(number, 0, nodeCount() - 1, "node number not in valid range");
        return definitionNodes.get(number);
    }

    //TODO test

    /**
     * Returns the the next node to the node with the specified number. Will skip the last node of a closed polygon
     * (because it's duplicate to the first one).
     */
    public Vector nextNode(final int nodeNr) {
        final int index = nodeNr == nodeCount() - 1 && isClosed()
                ? 0
                : nodeNr + 1;
        return node(index);
    }

    //TODO test
    /**
     * Returns the the previous node to the node with the specified number. Will skip the first node of a closed polygon
     * (because it's duplicate to the last one).
     */
    public Vector previousNode(final int nodeNr) {
        final int index = nodeNr == 0 && isClosed()
                ? nodeCount() - 1
                : nodeNr - 1;
        return node(index);
    }

    public boolean isOpen() {
        return !isClosed();
    }

    private List<Vector> initializeNodes() {
        return isOpen() ? definitionNodes : definitionNodes.subList(1, definitionNodes.size());
    }

    private List<Line> initializeSegments() {
        final var segmentsValue = new ArrayList<Line>();
        for (int i = 0; i < definitionNodes.size() - 1; i++) {
            final var segment = Line.between(definitionNodes.get(i), definitionNodes.get(i + 1));
            segmentsValue.add(segment);
        }
        return unmodifiableList(segmentsValue);
    }

    private Vector initializeCenter() {
        double x = 0;
        double y = 0;
        for (final var node : nodes()) {
            x += node.x();
            y += node.y();
        }
        return Vector.$(x / nodeCount(), y / nodeCount());
    }
}

package dev.screwbox.core;

import dev.screwbox.core.utils.Validate;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static dev.screwbox.core.Vector.$;
import static dev.screwbox.core.utils.ListUtil.combine;
import static dev.screwbox.core.utils.MathUtil.isUneven;
import static java.lang.Math.PI;
import static java.util.Collections.unmodifiableList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * A polygon shape within the game world made of a list of {@link Vector nodes}. Can be closed or open.
 *
 * @since 3.17.0
 */
public final class Polygon implements Serializable {

    // Some point that is considered never to reside within a polygon
    private static final Vector POINT_OUTSIDE_POLYGON = $(Double.MAX_VALUE / 1_000_000_099.123, Double.MAX_VALUE / 1_000_023.456);
    private static final double BISECTOR_CHECK_LENGTH = Double.MAX_VALUE / 1_000_000_099.123;

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<Vector> definitionNodes;
    private transient List<Vector> nodes;
    private transient List<Line> segments;
    private transient Vector center;

    /**
     * Create a new instance from the specified nodes. Requires at least one node.
     *
     * @since 3.20.0
     */
    public static Polygon ofNodes(final Vector... nodes) {
        return ofNodes(List.of(nodes));
    }

    /**
     * Create a new instance from the specified list of nodes. Requires at least one node.
     */
    public static Polygon ofNodes(final List<Vector> nodes) {
        return new Polygon(nodes);
    }

    private Polygon(final List<Vector> nodes) {
        Validate.notEmpty(nodes, "polygon must have at least one node");
        this.definitionNodes = unmodifiableList(nodes);
    }

    /**
     * Returns the segments between the nodes.
     */
    public List<Line> segments() {
        if (isNull(segments)) {
            final var segmentsValue = new ArrayList<Line>();
            for (int i = 0; i < definitionNodes.size() - 1; i++) {
                final var segment = Line.between(definitionNodes.get(i), definitionNodes.get(i + 1));
                segmentsValue.add(segment);
            }
            segments = unmodifiableList(segmentsValue);
        }
        return segments;
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
        return Polygon.ofNodes(combine(definitionNodes, node));
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
        if (isNull(nodes)) {
            nodes = isOpen()
                ? definitionNodes
                : definitionNodes.subList(1, definitionNodes.size());
        }
        return nodes;
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
     * Returns {@code true} if the polygon is closed (last node is connected to first node).
     */
    public boolean isClosed() {
        return definitionNodes.size() != 1 && firstNode().equals(lastNode());
    }

    /**
     * Returns {@code true} if the polygon is open (last node is not connected to first node).
     */
    public boolean isOpen() {
        return !isClosed();
    }

    /**
     * Returns {@code true} if the specified position is within the polygon. Will be {@code false} if polygon {@link #isOpen()}.
     */
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
        if (isNull(center)) {
            double x = 0;
            double y = 0;
            for (final var node : nodes()) {
                x += node.x();
                y += node.y();
            }
            center = $(x / nodeCount(), y / nodeCount());
        }
        return center;
    }

    /**
     * Returns {@code true} if the {@link Polygon} nodes are oriented clockwise.
     * Will be false if {@link #isOpen()} or has less than three nodes.
     *
     * @see <a href="https://en.wikipedia.org/wiki/Shoelace_formula">Shoelace formula</a>
     */
    public boolean isClockwise() {
        if (isOpen() || nodeCount() < 3) {
            return false;
        }
        return shoelaceSum() >= 0;
    }

    /**
     * Returns the area of a closed polygon.
     * Will be zero if polygon {@link #isOpen()} or has less than three nodes.
     *
     * @see <a href="https://en.wikipedia.org/wiki/Shoelace_formula">Shoelace formula</a>
     * @since 3.20.0
     */
    public double area() {
        if (isOpen() || nodeCount() < 3) {
            return 0;
        }
        double sum = shoelaceSum();
        return Math.abs(sum / 2.0);
    }

    private Double shoelaceSum = null;

    private double shoelaceSum() {
        if (isNull(shoelaceSum)) {
            double sum = 0;
            for (final var segment : segments()) {
                sum += segment.start().x() * segment.end().y() - segment.end().x() * segment.start().y();
            }
            shoelaceSum = sum;
        }
        return shoelaceSum;
    }

    /**
     * Returns the bisector ray between a node and the opposite side of the {@link Polygon}.
     * Will be empty if the bisector ray does not hit the {@link Polygon}.
     *
     * @see <a href="https://en.wikipedia.org/wiki/Angle_bisector_theorem">Angle bisector theorem</a>
     */
    public Optional<Line> bisectorRay(final int nodeNr) {
        final Line ray = calculateBisectorRayFullLength(nodeNr);
        double minDistance = Double.MAX_VALUE;
        Line bisectorRay = null;
        for (final var segment : segments()) {
            if (!segment.start().equals(ray.start()) && !segment.end().equals(ray.start())) {
                final Vector intersectPoint = ray.intersectionPoint(segment);
                if (nonNull(intersectPoint)) {
                    Line currentRay = Line.between(ray.start(), intersectPoint);
                    double distance = currentRay.length();
                    if (distance < minDistance) {
                        bisectorRay = currentRay;
                        minDistance = distance;
                    }
                }
            }
        }
        return Optional.ofNullable(bisectorRay);
    }

    private Line calculateBisectorRayFullLength(final int nodeNr) {
        if (isOpen()) {
            if (nodeNr == 0) {
                Line first = segments().getFirst();
                return Angle.of(first)
                    .addDegrees(90)
                    .applyOn(Line.normal(first.start(), BISECTOR_CHECK_LENGTH));
            }
            if (nodeNr == nodeCount() - 1) {
                Line last = segments().getLast();
                return Angle.of(last)
                    .addDegrees(90)
                    .applyOn(Line.normal(last.end(), BISECTOR_CHECK_LENGTH));
            }
        }
        final Vector node = node(nodeNr);
        final Vector previousNode = previousNode(nodeNr);
        final Vector nextNode = nextNode(nodeNr);

        final double degrees = Angle.betweenLines(node, previousNode, nextNode).degrees() / 2.0
                               + (isClockwise() ? 180 : 0);
        return Angle.ofLineBetweenPoints(node, nextNode)
            .addDegrees(degrees)
            .applyOn(Line.normal(node, BISECTOR_CHECK_LENGTH));
    }

    /**
     * Returns the specified {@link Vector node}.
     *
     * @since 2.20.0
     */
    public Vector node(int number) {
        Validate.range(number, 0, nodeCount() - 1, "node number not in valid range");
        return definitionNodes.get(number);
    }

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

    /**
     * Returns the index of the node on the opposite side of the {@link Polygon} from the node with the specified index.
     * Will be empty if {@link Polygon#isOpen()}.
     */
    public Optional<Integer> opposingIndex(final int index) {
        if (isOpen()) {
            return Optional.empty();
        }
        return bisectorRay(index)
            .map(ray -> nearestIndex(ray.end()))
            .filter(res -> res != index);
    }

    /**
     * Returns the index of the node within the {@link Polygon} that is the nearest to the specified position.
     */
    public int nearestIndex(final Vector position) {
        double nearestDistance = definitionNodes.getFirst().distanceTo(position);
        int nearestIndex = 0;
        int index = 0;
        for (final var other : definitionNodes) {
            final double distance = position.distanceTo(other);
            if (distance < nearestDistance) {
                nearestIndex = index;
                nearestDistance = other.distanceTo(position);
            }
            index++;
        }
        return nearestIndex;
    }

    /**
     * Aligns the specified template as close as possible to the current shape. Requires both {@link Polygon polygons}
     * to contains same amount of nodes. It's possible to specify if the template {@link Polygon} may be rotated and or moved.
     *
     * @since 3.18.0
     */
    public Polygon alignTemplate(final Polygon template, final boolean useRotation, final boolean useMotion) {
        Validate.isEqual(template.nodeCount(), nodeCount(), "both polygons must have same node count for alignment");
        if (!useRotation && !useMotion) {
            return template;
        }
        final var polygonRotation = useRotation ? averageRotationDifferenceTo(template) : Angle.none();
        final var polygonShift = useMotion ? center().substract(template.center()) : Vector.zero();

        final List<Vector> matchNodes = new ArrayList<>();
        for (final var node : template.definitionNotes()) {
            final Vector rotatedNode = polygonRotation.rotatePointAroundCenter(node, template.center());
            matchNodes.add(rotatedNode.add(polygonShift));
        }
        return Polygon.ofNodes(matchNodes);
    }

    private Angle averageRotationDifferenceTo(final Polygon other) {
        Double lastDiff = null;
        double totalCumulativeRotation = 0;
        for (int i = 0; i < nodes().size(); i++) {
            final Angle otherCenterLine = Angle.ofLineBetweenPoints(other.center(), other.node(i));
            final Angle centerLine = Angle.ofLineBetweenPoints(center(), node(i));

            double currentDiff = otherCenterLine.delta(centerLine).radians();
            if (nonNull(lastDiff)) {
                if (currentDiff - lastDiff > PI) {
                    currentDiff -= 2 * PI;
                } else if (currentDiff - lastDiff < -PI) {
                    currentDiff += 2 * PI;
                }
            }

            lastDiff = currentDiff;
            totalCumulativeRotation += currentDiff;
        }
        return Angle.radians(totalCumulativeRotation / nodes().size());
    }

    /**
     * Returns a closed version of the {@link Polygon}. Will return unchanged version, if the {@link Polygon} is already {@link #close() closed}.
     *
     * @since 3.20.0
     */
    public Polygon close() {
        return isClosed()
            ? this
            : Polygon.ofNodes(combine(definitionNotes(), firstNode()));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Polygon polygon = (Polygon) o;
        return Objects.equals(definitionNodes, polygon.definitionNodes);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(definitionNodes);
    }
}
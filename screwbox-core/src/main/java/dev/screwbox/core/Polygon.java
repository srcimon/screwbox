package dev.screwbox.core;

import dev.screwbox.core.utils.ListUtil;
import dev.screwbox.core.utils.Validate;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static dev.screwbox.core.Vector.$;
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
     * Create a new instance from the specified nodes. Needs at least one node.
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
                               + (isOrientedClockwise() ? 180 : 0);
        return Angle.of(Line.between(node, nextNode))
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
     */
    public Optional<Integer> opposingIndex(final int index) {
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

    //TODO changelog
    public Polygon alignTemplate(final Polygon template, boolean useRotation) {
        Validate.isEqual(template.nodeCount(), nodeCount(), "both polygons must have same node count");
        final var polygonRotation = useRotation ? averageRotationDifferenceTo(template) : Angle.none();
        final var polygonShift = center().substract(template.center());
        final List<Vector> matchNodes = new ArrayList<>();
        for (final var node : template.definitionNotes()) {
            //TODO Angle.rotateAroundCenter
           final Vector rotatedNode = useRotation
                    ? polygonRotation.applyOn(Line.between(template.center(), node)).end()
                    : node;
            matchNodes.add(rotatedNode.add(polygonShift));
        }
        return Polygon.ofNodes(matchNodes);
    }


    private Angle averageRotationDifferenceTo(final Polygon other) {
        Double lastDiff = null;
        double totalCumulativeRotation = 0;
        for (int i = 0; i < nodes().size(); i++) {
            double angleA = Math.atan2(other.node(i).y() - other.center().y(), other.node(i).x() - other.center().x());
            double angleB = Math.atan2(node(i).y() - center().y(), node(i).x() - center().x());

            double currentDiff = angleB - angleA;

            while (currentDiff <= -PI) currentDiff += 2 * PI;
            while (currentDiff > PI) currentDiff -= 2 * PI;

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
        double averageRotationRadians = totalCumulativeRotation / (double) nodes().size();

        while (averageRotationRadians <= -PI) averageRotationRadians += 2 * PI;
        while (averageRotationRadians > PI) averageRotationRadians -= 2 * PI;

        return Angle.degrees(Math.toDegrees(averageRotationRadians));
    }
}

package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Bounds;
import dev.screwbox.core.Line;
import dev.screwbox.core.Vector;
import dev.screwbox.core.navigation.Borders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Comparator.comparingDouble;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public class LightPhysics {

    private static class Occluder {
        protected final Bounds bounds;
        protected List<Line> lines;

        Occluder(final Bounds bounds) {
            requireNonNull(bounds, "occluder must not be null");
            this.bounds = bounds;
        }

        public List<Line> lines(Vector lightPosition) {
            if (lines == null) {
                lines = Borders.ALL.extractFrom(bounds);
            }
            return lines;
        }
    }

    private static class NoSelfOccluder extends Occluder {
        NoSelfOccluder(final Bounds bounds) {
            super(bounds);
        }

        public List<Line> lines(Vector lightPosition) {
            if (lines == null) {
                lines = new ArrayList<>(Borders.ALL.extractFrom(bounds));
            }
            final List<Line> lightDependendLines = new ArrayList<>();
            final boolean isBetweenX = lightPosition.x() > bounds.minX() && lightPosition.x() < bounds.maxX();
            final boolean isBetweenY = lightPosition.y() > bounds.minY() && lightPosition.y() < bounds.maxY();
            lines.sort(comparingDouble(border -> border.center().distanceTo(lightPosition)));
            if (isBetweenX != isBetweenY) {
                lightDependendLines.add(lines.get(lines.get(1).intersects(Line.between(bounds.position(), lightPosition)) ? 0 : 1));
            }
            lightDependendLines.add(lines.get(2));
            lightDependendLines.add(lines.get(3));
            return lightDependendLines;
        }
    }

    private final List<Occluder> occluders = new ArrayList<>();

    public void addOccluder(final Bounds occluder) {
        occluders.add(new Occluder(occluder));
    }

    public void addNoSelfOccluder(final Bounds occluder) {
        occluders.add(new NoSelfOccluder(occluder));
    }

    public boolean isOccluded(final Line source) {
        final var box = new DirectionalLightBox(source, 1);
        for (final var occluder : occluders) {
            if (box.intersects(occluder.bounds)) {
                return true;
            }
        }
        return false;
    }

    public boolean isOccluded(final Vector position) {
        for (final var occluder : occluders) {
            if (occluder.bounds.contains(position)) {
                return true;
            }
        }
        return false;
    }

    public List<Vector> calculateArea(final Bounds lightBox, double minAngle, double maxAngle) {
        final Line normal = Line.normal(lightBox.position(), -lightBox.height() / 2.0);
        final List<Vector> area = new ArrayList<>();
        if (minAngle != 0 || maxAngle != 360) {
            area.add(lightBox.position());
        }
        final var relevantOccluders = allIntersecting(lightBox);
        for (long angle = Math.round(minAngle); angle < maxAngle; angle++) {
            final Line raycast = Angle.degrees(angle).rotate(normal);
            area.add(findClosest(relevantOccluders, raycast));
        }
        return area;
    }

    private List<Occluder> allIntersecting(Bounds box) {
        final List<Occluder> intersecting = new ArrayList<>();
        for (final var occluder : occluders) {
            if (occluder.bounds.intersects(box)) {
                intersecting.add(occluder);
            }
        }
        return intersecting;
    }

    public List<Vector> calculateArea(final DirectionalLightBox lightBox) {
        final List<Occluder> relevantOccluders = new ArrayList<>();
        for (final var occluder : occluders) {
            if (lightBox.intersects(occluder.bounds)) {
                relevantOccluders.add(occluder);
            }
        }

        final List<Line> definitionLines = new ArrayList<>();
        for (final var probe : calculateLightProbes(lightBox, relevantOccluders)) {
            Vector closest1 = findClosest(relevantOccluders, probe);
            definitionLines.add(closest1 == null ? probe : Line.between(probe.start(), closest1));
        }
        List<ScoredPoint> points = new ArrayList<>();
        for (final var line : definitionLines) {
            points.add(new ScoredPoint(line.end(), line.start().distanceTo(lightBox.origin())));
        }
        Collections.sort(points);
        final List<Vector> area = new ArrayList<>(definitionLines.size() + 2);//TODO avoid second list
        for (final var point : points) {
            area.add(point.position);
        }
        area.add(lightBox.topRight());
        area.add(lightBox.origin());
        return area;
    }

    record ScoredPoint(Vector position, double cost) implements Comparable<ScoredPoint> {
        @Override
        public int compareTo(ScoredPoint o) {
            return Double.compare(cost, o.cost);
        }
    }

    private static Vector findClosest(List<Occluder> relevantOccluders, Line raycast) {
        double minDist = Double.MAX_VALUE;
        Vector nearest = null;
        for (final var occluder : relevantOccluders) {
            final List<Line> lines = occluder.lines(raycast.start());
            for (final var other : lines) {
                final var intersectionPoint = raycast.intersectionPoint(other);
                if (nonNull(intersectionPoint)) {
                    var distance = raycast.start().distanceTo(intersectionPoint);
                    if (distance < minDist) {
                        minDist = distance;
                        nearest = intersectionPoint;
                    }
                }
            }
        }
        return nearest == null ? raycast.end() : nearest;
    }

    private static List<Line> calculateLightProbes(DirectionalLightBox lightBox, List<Occluder> relevantOccluders) {
        final List<Line> lightProbes = new ArrayList<>();
        final var a = lightBox.source().end().substract(lightBox.source().start()).length(0.000000000001);
        final var b = lightBox.source().start().substract(lightBox.source().end()).length(0.000000000001);

        for (final var occluder : relevantOccluders) {
            processPoint(lightBox, occluder.bounds.origin(), lightProbes, a, b);
            processPoint(lightBox, occluder.bounds.topRight(), lightProbes, a, b);
            processPoint(lightBox, occluder.bounds.bottomRight(), lightProbes, a, b);
            processPoint(lightBox, occluder.bounds.bottomLeft(), lightProbes, a, b);
        }

        lightProbes.add(Line.between(lightBox.origin(), lightBox.bottomLeft()));
        lightProbes.add(Line.between(lightBox.topRight(), lightBox.bottomRight()));
        return lightProbes;
    }

    private static void processPoint(DirectionalLightBox lightBox, Vector p, List<Line> lightProbes, Vector a, Vector b) {
        lightBox.source().perpendicular(p).ifPresent(perpendicular -> {
                lightProbes.add(perpendicular.move(a).length(lightBox.distance()));
                lightProbes.add(perpendicular);
                lightProbes.add(perpendicular.move(b).length(lightBox.distance()));
            }
        );
    }
}

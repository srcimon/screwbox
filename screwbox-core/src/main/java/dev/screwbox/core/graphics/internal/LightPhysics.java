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

        public List<Line> lines(final Vector sourcePosition) {
            if (lines == null) {
                lines = new ArrayList<>(Borders.ALL.extractFrom(bounds));
            }
            final List<Line> pointSpecificLights = new ArrayList<>();
            final boolean isBetweenX = sourcePosition.x() > bounds.minX() && sourcePosition.x() < bounds.maxX();
            final boolean isBetweenY = sourcePosition.y() > bounds.minY() && sourcePosition.y() < bounds.maxY();
            lines.sort(comparingDouble(border -> border.center().distanceTo(sourcePosition)));
            if (isBetweenX != isBetweenY) {
                pointSpecificLights.add(lines.get(lines.get(1).intersects(Line.between(bounds.position(), sourcePosition)) ? 0 : 1));
            }
            pointSpecificLights.add(lines.get(2));
            pointSpecificLights.add(lines.get(3));
            return pointSpecificLights;
        }
    }

    private static class SelfOccluder extends Occluder {

        SelfOccluder(final Bounds bounds) {
            super(bounds);
        }

        @Override
        public List<Line> lines(final Vector lightPosition) {
            if (lines == null) {
                lines = Borders.ALL.extractFrom(bounds);
            }
            return lines;
        }
    }

    private final List<Occluder> occluders = new ArrayList<>();

    public void addOccluder(final Bounds occluder) {
        occluders.add(new SelfOccluder(occluder));
    }

    public void addNoSelfOccluder(final Bounds occluder) {
        occluders.add(new Occluder(occluder));
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
            area.add(findNearest(raycast, relevantOccluders));
        }
        return area;
    }

    private List<Occluder> allIntersecting(final Bounds bounds) {
        final List<Occluder> intersecting = new ArrayList<>();
        for (final var occluder : occluders) {
            if (occluder.bounds.intersects(bounds)) {
                intersecting.add(occluder);
            }
        }
        return intersecting;
    }

    List<Vector> calculateArea(final DirectionalLightBox lightBox) {
        final List<Occluder> relevantOccluders = new ArrayList<>();
        for (final var occluder : occluders) {
            if (lightBox.intersects(occluder.bounds)) {
                relevantOccluders.add(occluder);
            }
        }

        final List<Line> definitionLines = new ArrayList<>();
        for (final var probe : calculateLightProbes(lightBox, relevantOccluders)) {
            Vector closest1 = findNearest(probe, relevantOccluders);
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

    private record ScoredPoint(Vector position, double cost) implements Comparable<ScoredPoint> {
        @Override
        public int compareTo(ScoredPoint o) {
            return Double.compare(cost, o.cost);
        }
    }

    private static Vector findNearest(final Line raycast, final List<Occluder> rayOccluders) {
        double minDist = Double.MAX_VALUE;
        Vector nearest = null;
        for (final var occluder : rayOccluders) {
            for (final var other : occluder.lines(raycast.start())) {
                final var intersection = raycast.intersectionPoint(other);
                if (nonNull(intersection)) {
                    var distance = raycast.start().distanceTo(intersection);
                    if (distance < minDist) {
                        minDist = distance;
                        nearest = intersection;
                    }
                }
            }
        }
        return nearest == null ? raycast.end() : nearest;
    }

    private static List<Line> calculateLightProbes(final DirectionalLightBox lightBox, final List<Occluder> lightOccluders) {
        final List<Line> lightProbes = new ArrayList<>();
        final var left = lightBox.source().end().substract(lightBox.source().start()).length(0.000000000001);
        final var right = lightBox.source().start().substract(lightBox.source().end()).length(0.000000000001);

        for (final var occluder : lightOccluders) {
            addProbes(lightBox, occluder.bounds.origin(), lightProbes, left, right);
            addProbes(lightBox, occluder.bounds.topRight(), lightProbes, left, right);
            addProbes(lightBox, occluder.bounds.bottomRight(), lightProbes, left, right);
            addProbes(lightBox, occluder.bounds.bottomLeft(), lightProbes, left, right);
        }

        lightProbes.add(Line.between(lightBox.origin(), lightBox.bottomLeft()));
        lightProbes.add(Line.between(lightBox.topRight(), lightBox.bottomRight()));
        return lightProbes;
    }

    private static void addProbes(final DirectionalLightBox lightBox, final Vector point, final List<Line> probes, final Vector left, final Vector right) {
        lightBox.source().perpendicular(point).ifPresent(perpendicular -> {
                probes.add(perpendicular.move(left).length(lightBox.distance()));
                probes.add(perpendicular);
                probes.add(perpendicular.move(right).length(lightBox.distance()));
            }
        );
    }
}

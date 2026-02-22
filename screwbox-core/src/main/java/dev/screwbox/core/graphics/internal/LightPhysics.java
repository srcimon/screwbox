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
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public class LightPhysics {

    private static final int INTELLIGENT_RAY_CALC_OCCLUDER_LIMIT = 30;
    private static final Angle LEFT_ROTATION = Angle.degrees(0.01);
    private static final Angle RIGHT_ROTATION = Angle.degrees(-0.01);

    private record FastSortingLine(Line line, double score) implements Comparable<FastSortingLine> {
        @Override
        public int compareTo(FastSortingLine o) {
            return Double.compare(score, o.score);
        }
    }

    private static class Occluder {
        protected final Bounds bounds;
        protected List<Line> lines;

        Occluder(final Bounds bounds) {
            requireNonNull(bounds, "occluder must not be null");
            this.bounds = bounds;
        }

        public List<Line> lines(final Vector sourcePosition) {
            if (isNull(lines)) {
                lines = new ArrayList<>(Borders.ALL.extractFrom(bounds));
            }
            synchronized (this) {
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
    }

    private static class AffectedByShadwowOccluder extends Occluder {

        AffectedByShadwowOccluder(final Bounds bounds) {
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

    public void addAffectedByShadowOccluder(final Bounds occluder) {
        occluders.add(new AffectedByShadwowOccluder(occluder));
    }

    public void addOccluder(final Bounds occluder) {
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
        final List<FastSortingLine> area = new ArrayList<>();

        final var relevantOccluders = allIntersecting(lightBox);
        for (final var probe : calculateLightProbes(lightBox, relevantOccluders, minAngle, maxAngle)) {
            final Line nearest = findNearest(probe, relevantOccluders);
            final double degrees = Angle.of(nearest).degrees();
            area.add(new FastSortingLine(nearest, degrees));
        }
        if (minAngle == 0 && maxAngle == 360) {
            Collections.sort(area);
        }
        final List<Vector> result = new ArrayList<>();
        for (var point : area) {
            result.add(point.line.end());
        }
        if (minAngle != 0 || maxAngle != 360) {
            result.add(lightBox.position());
        }
        return result;
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

        final List<FastSortingLine> definitionLines = new ArrayList<>();
        for (final var probe : calculateLightProbes(lightBox, relevantOccluders)) {
            final Line nearest = findNearest(probe, relevantOccluders);
            final double score = nearest.start().distanceTo(lightBox.origin());
            definitionLines.add(new FastSortingLine(nearest, score));
        }
        definitionLines.sort(null);
        final List<Vector> area = new ArrayList<>(definitionLines.size() + 2);
        for (final var line : definitionLines) {
            area.add(line.line.end());
        }
        area.add(lightBox.topRight());
        area.add(lightBox.origin());
        return area;
    }

    private static Line findNearest(final Line raycast, final List<Occluder> rayOccluders) {
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
        return nearest == null ? raycast : Line.between(raycast.start(), nearest);
    }

    private static List<Line> calculateLightProbes(final Bounds lightBox, final List<Occluder> lightOccluders, double minAngle, double maxAngle) {
        final List<Line> lightProbes = new ArrayList<>();
        if (minAngle != 0 || maxAngle != 360 || lightOccluders.size() > INTELLIGENT_RAY_CALC_OCCLUDER_LIMIT) {
            final Line normal = Line.normal(lightBox.position(), -lightBox.height() / 2.0);
            for (long angle = Math.round(minAngle); angle < maxAngle; angle++) {
                final Line raycast = Angle.degrees(angle).rotate(normal);
                lightProbes.add(findNearest(raycast, lightOccluders));
            }
            return lightProbes;
        }
        for (final var occluder : lightOccluders) {
            addProbes(lightBox, occluder.bounds.origin(), lightProbes);
            addProbes(lightBox, occluder.bounds.topRight(), lightProbes);
            addProbes(lightBox, occluder.bounds.bottomRight(), lightProbes);
            addProbes(lightBox, occluder.bounds.bottomLeft(), lightProbes);
        }

        lightProbes.add(Line.between(lightBox.position(), lightBox.bottomLeft()));
        lightProbes.add(Line.between(lightBox.position(), lightBox.bottomRight()));
        lightProbes.add(Line.between(lightBox.position(), lightBox.origin()));
        lightProbes.add(Line.between(lightBox.position(), lightBox.topRight()));

        return lightProbes;
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

    private static void addProbes(final Bounds lightBox, final Vector point, final List<Line> probes) {
        Line between = Line.between(lightBox.position(), point).length(lightBox.width());
        probes.add(LEFT_ROTATION.rotate(between).length(lightBox.width()));
        probes.add(RIGHT_ROTATION.rotate(between).length(lightBox.width()));
        probes.add(between);
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

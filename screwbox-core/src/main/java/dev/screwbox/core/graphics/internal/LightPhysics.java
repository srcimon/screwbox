package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Bounds;
import dev.screwbox.core.Line;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.graphics.options.OvalDrawOptions;
import dev.screwbox.core.navigation.Borders;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.Comparator.comparingDouble;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public class LightPhysics {

    private final List<Bounds> occluders = new ArrayList<>();
    private final List<Bounds> noSelfOccluders = new ArrayList<>();

    public void addOccluder(final Bounds occluder) {
        requireNonNull(occluder, "occluder must not be null");
        occluders.add(occluder);
    }

    public void addNoSelfOccluder(final Bounds occluder) {
        requireNonNull(occluder, "occluder must not be null");
        noSelfOccluders.add(occluder);
    }

    public boolean isOccluded(final Vector position) {
        for (final var occluder : occluders) {
            if (occluder.contains(position)) {
                return true;
            }
        }
        for (final var occluder : noSelfOccluders) {
            if (occluder.contains(position)) {
                return true;
            }
        }
        return false;
    }

    public List<Vector> calculateArea(final Bounds lightBox, double minAngle, double maxAngle) {
        final var relevantOccluders = lightBox.allIntersecting(occluders);
        final var relevantNoSelfOccluders = lightBox.allIntersecting(noSelfOccluders);
        final Line normal = Line.normal(lightBox.position(), -lightBox.height() / 2.0);
        final List<Line> occluderOutlines = extractLines(relevantOccluders);
        addFarDistanceLines(occluderOutlines, relevantNoSelfOccluders, lightBox.position());
        final List<Vector> area = new ArrayList<>();
        if (minAngle != 0 || maxAngle != 360) {
            area.add(lightBox.position());
        }
        for (long angle = Math.round(minAngle); angle < maxAngle; angle++) {
            final Line raycast = Angle.degrees(angle).applyOn(normal);
            Vector nearestPoint = raycast.end();
            double nearestDistance = raycast.end().distanceTo(lightBox.position());
            for (final var line : occluderOutlines) {
                final Vector intersectionPoint = line.intersectionPoint(raycast);
                if (nonNull(intersectionPoint)) {
                    final double distance = intersectionPoint.distanceTo(lightBox.position());
                    if (distance < nearestDistance) {
                        nearestPoint = intersectionPoint;
                        nearestDistance = distance;
                    }
                }
            }
            area.add(nearestPoint);
        }
        return area;
    }

    private static void addFarDistanceLines(final List<Line> allLines, final List<Bounds> allBounds, final Vector position) {
        for (final var bounds : allBounds) {
            final boolean isBetweenX = position.x() > bounds.minX() && position.x() < bounds.maxX();
            final boolean isBetweenY = position.y() > bounds.minY() && position.y() < bounds.maxY();
            final List<Line> borders = new ArrayList<>(Borders.ALL.extractFrom(bounds));
            borders.sort(comparingDouble(border -> border.center().distanceTo(position)));
            if (isBetweenX != isBetweenY) {
                allLines.add(borders.get(borders.get(1).intersects(Line.between(bounds.position(), position)) ? 0 : 1));
            }
            allLines.add(borders.get(2));
            allLines.add(borders.get(3));
        }
    }

    private static List<Line> extractLines(final List<Bounds> allBounds) {
        final List<Line> allLines = new ArrayList<>();
        for (final var bounds : allBounds) {
            allLines.addAll(Borders.ALL.extractFrom(bounds));
        }
        return allLines;
    }

    //TODO implement no self occluders
    public List<Vector> calculateArea(final DirectionalLightBox lightBox) {
//TODO remove source and distance parameters

        final List<Vector> poi = new ArrayList<>();

        for (final var occluder : occluders) {
            if (lightBox.contains(occluder.origin())) {
                poi.add(occluder.origin());
            }
            if (lightBox.contains(occluder.topRight())) {
                poi.add(occluder.topRight());
            }
            if (lightBox.contains(occluder.bottomRight())) {
                poi.add(occluder.bottomRight());
            }
            if (lightBox.contains(occluder.bottomLeft())) {
                poi.add(occluder.bottomLeft());
            }
        }

        List<Line> occluderOutlines = extractLines(occluders);
        occluderOutlines.add(Line.between(lightBox.bottomLeft(), lightBox.bottomRight()));
        List<Line> lightProbes = new ArrayList<>();
        for (final var p : poi) {
            lightBox.source().perpendicular(p).ifPresent(lightProbes::add);
        }
        var a = lightBox.source().end().substract(lightBox.source().start()).length(2);
        var b = lightBox.source().start().substract(lightBox.source().end()).length(2);
        for (final var perpendicular : new ArrayList<>(lightProbes)) {
            var left = perpendicular.move(a).length(lightBox.distance());
            var right = perpendicular.move(b).length(lightBox.distance());
            var leftTarget = left.closestIntersectionToStart(occluderOutlines);
            lightProbes.add(leftTarget.map(vector -> Line.between(left.start(), vector)).orElse(left));
            var rightTarget = right.closestIntersectionToStart(occluderOutlines);
            lightProbes.add(rightTarget.map(vector -> Line.between(right.start(), vector)).orElse(right));
        }
        lightProbes.add(Line.between(lightBox.origin(), lightBox.bottomLeft()));
        lightProbes.add(Line.between(lightBox.topRight(), lightBox.bottomRight()));

        final List<Line> definitionLines = new ArrayList<>();
        for (final var probe : lightProbes) {
//            DefaultWorld.DEBUG_WORKAROUND.drawLine(probe, LineDrawOptions.color(Color.WHITE.opacity(0.25)).drawOrder(Order.DEBUG_OVERLAY_LATE.drawOrder()));
            probe.closestIntersectionToStart(occluderOutlines).ifPresent(closest ->
                definitionLines.add(Line.between(probe.start(), closest)));
        }
        definitionLines.sort(new Comparator<Line>() {
            @Override
            public int compare(Line o1, Line o2) {
                return Double.compare(o1.start().distanceTo(lightBox.origin()), o2.start().distanceTo(lightBox.origin()));
            }
        });
        final List<Vector> area = new ArrayList<>();
        definitionLines.stream().map(d -> d.end()).forEach(area::add);
        area.add(lightBox.topRight());
        area.add(lightBox.origin());
//        area.add(lightBox.bottomRight());
//        area.add(lightBox.bottomLeft());
        return area;
    }
}

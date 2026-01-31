package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Bounds;
import dev.screwbox.core.Line;
import dev.screwbox.core.Vector;
import dev.screwbox.core.navigation.Borders;

import java.util.ArrayList;
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

    public List<Vector> calculateArea(final Line source, final double distance, final Angle direction) {
        final List<Vector> area = new ArrayList<>();
        //TODO FIX PSEUDOCODE
        area.add(source.start());
        area.add(source.end());
        area.add(direction.rotatePointAroundCenter(source.end().addY(distance), source.end()));
        area.add(direction.rotatePointAroundCenter(source.start().addY(distance), source.start()));
        return area;
    }
}

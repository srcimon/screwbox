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
        this.occluders.add(occluder);
    }

    public void addNoSelfOccluder(final Bounds occluder) {
        requireNonNull(occluder, "occluder must not be null");
        this.noSelfOccluders.add(occluder);
    }

    public boolean isOccluded(final Bounds bounds) {
        for (final var occluder : occluders) {
            if (occluder.contains(bounds)) {
                return true;
            }
        }
        for (final var occluder : noSelfOccluders) {
            if (occluder.contains(bounds)) {
                return true;
            }
        }
        return false;
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
        final List<Vector> area = new ArrayList<>();
        final Line normal = Line.normal(lightBox.position(), -lightBox.height() / 2.0);
        final List<Line> occluderOutlines = extractLines(relevantOccluders);
        occluderOutlines.addAll(extractFarDistanceLines(relevantNoSelfOccluders, lightBox.position()));
        if (minAngle != 0 || maxAngle != 360) {
            area.add(lightBox.position());
        }
        for (long angle = Math.round(minAngle); angle < maxAngle; angle++) {
            final Line raycast = Angle.degrees(angle).applyOn(normal);
            Vector nearestPoint = raycast.to();
            double nearestDistance = raycast.to().distanceTo(lightBox.position());
            for (final var line : occluderOutlines) {
                final Vector intersectionPoint = line.intersectionPoint(raycast);
                if (nonNull(intersectionPoint)
                    && intersectionPoint.distanceTo(lightBox.position()) < nearestDistance) {
                    nearestPoint = intersectionPoint;
                    nearestDistance = nearestPoint.distanceTo(lightBox.position());
                }
            }
            area.add(nearestPoint);
        }
        return area;
    }

    private List<Line> extractFarDistanceLines(final List<Bounds> allBounds, final Vector position) {
        final List<Line> allLines = new ArrayList<>();
        for (final var bounds : allBounds) {
            final boolean isBetweenX = position.x() > bounds.minX() && position.x() < bounds.maxX();
            final boolean isBetweenY = position.y() > bounds.minY() && position.y() < bounds.maxY();
            final List<Line> borders = new ArrayList<>(Borders.ALL.extractFrom(bounds));
            borders.sort(comparingDouble(border -> border.middle().distanceTo(position)));
            if (isBetweenX != isBetweenY) {
                allLines.add(borders.get(borders.get(1).intersects(Line.between(bounds.position(), position)) ? 0 : 1));
            }
            allLines.add(borders.get(2));
            allLines.add(borders.get(3));
        }
        return allLines;
    }

    private List<Line> extractLines(final List<Bounds> allBounds) {
        final List<Line> allLines = new ArrayList<>();
        for (final var bounds : allBounds) {
            allLines.addAll(Borders.ALL.extractFrom(bounds));
        }
        return allLines;
    }
}

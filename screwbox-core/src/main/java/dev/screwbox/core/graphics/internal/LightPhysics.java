package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Line;
import dev.screwbox.core.Rotation;
import dev.screwbox.core.Vector;
import dev.screwbox.core.physics.Borders;

import java.util.ArrayList;
import java.util.List;

import static java.util.Comparator.comparingDouble;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public class LightPhysics {

    private final List<Bounds> shadowCasters = new ArrayList<>();
    private final List<Bounds> noSelfShadowShadowCasters = new ArrayList<>();

    public void addShadowCaster(final Bounds shadowCaster) {
        requireNonNull(shadowCaster, "shadowCaster must not be null");
        this.shadowCasters.add(shadowCaster);
    }

    public void addNoSelfShadowShadowCasters(final Bounds shadowCaster) {
        requireNonNull(shadowCaster, "shadowCaster must not be null");
        this.noSelfShadowShadowCasters.add(shadowCaster);
    }

    public void clear() {
        shadowCasters.clear();
        noSelfShadowShadowCasters.clear();
    }

    public boolean isCoveredByShadowCasters(final Vector position) {
        for (final var bounds : shadowCasters) {
            if (bounds.contains(position)) {
                return true;
            }
        }
        for (final var bounds : noSelfShadowShadowCasters) {
            if (bounds.contains(position)) {
                return true;
            }
        }
        return false;
    }

    public List<Vector> calculateArea(final Bounds lightBox, double minAngle, double maxAngle) {
        final var relevantShadowCasters = lightBox.allIntersecting(shadowCasters);
        final var relevantNoSelfShadowCasters = lightBox.allIntersecting(noSelfShadowShadowCasters);
        final List<Vector> area = new ArrayList<>();
        final Line normal = Line.normal(lightBox.position(), -lightBox.height() / 2.0);
        final List<Line> shadowCasterLines = extractLines(relevantShadowCasters);
        shadowCasterLines.addAll(extractFarDistanceLines(relevantNoSelfShadowCasters, lightBox.position()));
        if (minAngle != 0 || maxAngle != 360) {
            area.add(lightBox.position());
        }
        for (long angle = Math.round(minAngle); angle < maxAngle; angle += 1) {
            final Line raycast = Rotation.degrees(angle).applyOn(normal);
            Vector nearestPoint = raycast.to();
            double nearestDistance = raycast.to().distanceTo(lightBox.position());
            for (final var line : shadowCasterLines) {
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

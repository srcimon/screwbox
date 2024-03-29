package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Line;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.physics.Borders;
import io.github.srcimon.screwbox.core.utils.ListUtil;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

class LightPhysics {

    private final List<Bounds> shadowCasters = new ArrayList<>();

    public void addShadowCasters(final List<Bounds> shadowCasters) {
        requireNonNull(shadowCasters, "shadowCasters must not be null");
        this.shadowCasters.addAll(shadowCasters);
    }

    public void addShadowCaster(final Bounds shadowCaster) {
        requireNonNull(shadowCaster, "shadowCaster must not be null");
        this.shadowCasters.add(shadowCaster);
    }

    public List<Bounds> shadowCasters() {
        return shadowCasters;
    }

    public List<Vector> calculateArea(final Bounds lightBox, double minAngle, double maxAngle) {
        final var relevantShadowCasters = lightBox.allIntersecting(shadowCasters);
        final List<Vector> area = new ArrayList<>();
        final Line normal = Line.normal(lightBox.position(), -lightBox.height() / 2.0);
        final List<Line> shadowCasterLines = extractLines(relevantShadowCasters);
        if (minAngle != 0 || maxAngle != 360) {
            area.add(lightBox.position());
        }
        for (long angle = Math.round(minAngle); angle < maxAngle; angle += 1) {
            final Line raycast = Rotation.degrees(angle).applyOn(normal);
            Vector nearestPoint = raycast.to();
            for (final var line : shadowCasterLines) {
                final Vector intersectionPoint = line.intersectionPoint(raycast);
                if (nonNull(intersectionPoint)
                        && intersectionPoint.distanceTo(lightBox.position()) < nearestPoint
                                .distanceTo(lightBox.position())) {
                    nearestPoint = intersectionPoint;
                }
            }
            area.add(nearestPoint);
        }
        return area;
    }

    private List<Line> extractLines(final List<Bounds> allBounds) {
        final List<Line> allLines = new ArrayList<>();
        for (final var bounds : allBounds) {
            ListUtil.addAll(allLines, Borders.ALL.extractFrom(bounds));
        }
        return allLines;
    }

    public boolean isCoveredByShadowCasters(final Vector position) {
        for (final var bounds : shadowCasters) {
            if (bounds.contains(position)) {
                return true;
            }
        }
        return false;
    }

}

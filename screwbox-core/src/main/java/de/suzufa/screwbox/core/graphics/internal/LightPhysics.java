package de.suzufa.screwbox.core.graphics.internal;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.Angle;
import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Segment;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.physics.Borders;

public class LightPhysics {

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

    public List<Vector> calculateArea(final Bounds lightBox) {
        final var relevantShadowCasters = lightBox.allIntersecting(shadowCasters);
        final List<Vector> area = new ArrayList<>();
        final Segment normal = Segment.between(lightBox.position(), lightBox.position().addY(-lightBox.height() / 2.0));
        for (double d = 0; d < 360; d += 1.5) {
            Segment raycast = Angle.degrees(d).rotate(normal);
            Vector nearestPoint = raycast.to();
            for (final var segment : getSegmentsOf(relevantShadowCasters)) {
                final Vector intersectionPoint = segment.intersectionPoint(raycast);
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

    private List<Segment> getSegmentsOf(final List<Bounds> allBounds) {
        final List<Segment> allSegments = new ArrayList<>();
        for (final var bounds : allBounds) {
            for (final var segment : Borders.ALL.extractSegmentsMethod().apply(bounds)) {
                allSegments.add(segment);
            }
        }
        return allSegments;
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

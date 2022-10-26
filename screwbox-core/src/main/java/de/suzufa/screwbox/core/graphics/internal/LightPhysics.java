package de.suzufa.screwbox.core.graphics.internal;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import de.suzufa.screwbox.core.Angle;
import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Segment;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.physics.Borders;

public class LightPhysics {

    private List<Bounds> shadowCasters = new ArrayList<>();
    private final DefaultWorld world;

    public LightPhysics(final DefaultWorld world) {
        this.world = world;
    }

    public void setShadowCasters(final List<Bounds> shadowCasters) {
        this.shadowCasters = requireNonNull(shadowCasters, "shadowCasters must not be null");
    }

    public List<Bounds> shadowCasters() {
        return shadowCasters;
    }

    public List<Offset> calculateArea(final Bounds lightBox) {
        final var relevantBlockingBounds = lightBox.allIntersecting(shadowCasters);
        final List<Offset> area = new ArrayList<>();
        final List<Segment> raycasts = getRelevantRaytraces(lightBox, relevantBlockingBounds);
        for (final var raycast : raycasts) {
            var mostDistantHit = raycast.to();
            var mostDistance = lightBox.position().distanceTo(mostDistantHit);

            for (final var segment : getSegmentsOf(relevantBlockingBounds)) {
                final Vector intersectionPoint = segment.intersectionPoint(raycast);
                if (intersectionPoint != null) {
                    final double distance = intersectionPoint.distanceTo(lightBox.position());
                    if (mostDistance < distance) {
                        mostDistantHit = intersectionPoint;
                        mostDistance = distance;
                    }
                }
            }
            area.add(world.toOffset(mostDistantHit));
        }
        return area;
    }

    private List<Segment> getRelevantRaytraces(final Bounds source, final List<Bounds> colliders) {
        final var segments = new ArrayList<Segment>();
        segments.add(Segment.between(source.position(), source.bottomLeft()));
        segments.add(Segment.between(source.position(), source.bottomRight()));
        segments.add(Segment.between(source.position(), source.topLeft()));
        segments.add(Segment.between(source.position(), source.topRight()));
        final var range = source.extents().length();
        for (final var collider : colliders) {
            segmentsOf(segments, source, range, collider.bottomLeft());
            segmentsOf(segments, source, range, collider.bottomRight());
            segmentsOf(segments, source, range, collider.topLeft());
            segmentsOf(segments, source, range, collider.topRight());
        }
        segments.sort(new Comparator<Segment>() {

            @Override
            public int compare(final Segment o1, final Segment o2) {
                return Angle.of(o1).compareTo(Angle.of(o2));
            }
        });
        return segments;
    }

    private void segmentsOf(final List<Segment> list, final Bounds source, final double range,
            final Vector destination) {
        final Segment directTrace = Segment.between(source.position(), destination);
        final Segment normalTrace = Segment.between(source.position(), source.position().addY(-range));
        final var rotationOfDirectTrace = Angle.of(directTrace).degrees();
        list.add(Angle.degrees(rotationOfDirectTrace - 0.01).rotate(normalTrace));
        list.add(directTrace);
        list.add(Angle.degrees(rotationOfDirectTrace + 0.01).rotate(normalTrace));
    }

    private List<Segment> getSegmentsOf(final List<Bounds> allBounds) {
        final List<Segment> allSegments = new ArrayList<>();
        for (final var bounds : allBounds) {
            for (final var x : Borders.ALL.extractSegmentsMethod().apply(bounds)) {
                allSegments.add(x);
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

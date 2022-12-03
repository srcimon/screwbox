package de.suzufa.screwbox.core.graphics.internal;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.suzufa.screwbox.core.Angle;
import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.Segment;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.physics.Borders;
import de.suzufa.screwbox.core.physics.internal.DistanceComparator;

public class LightPhysics {

    private static final Comparator<Segment> BY_ANGLE = (first, second) -> Angle.of(first).compareTo(Angle.of(second));

    private final List<Bounds> shadowCasters = new ArrayList<>();

    public void addShadowCasters(final List<Bounds> shadowCasters) {
        requireNonNull(shadowCasters, "shadowCasters must not be null");
        this.shadowCasters.addAll(shadowCasters);
    }

    public void addShadowCaster(Bounds shadowCaster) {
        requireNonNull(shadowCaster, "shadowCaster must not be null");
        this.shadowCasters.add(shadowCaster);
    }

    public List<Bounds> shadowCasters() {
        return shadowCasters;
    }

    public List<Vector> calculateArea(final Bounds lightBox) {
        final var relevantShadowCasters = lightBox.allIntersecting(shadowCasters);
        final List<Segment> raycasts = getRelevantRaytraces(lightBox, relevantShadowCasters);
        final List<Segment> segments = getSegmentsOf(relevantShadowCasters);
        final List<Vector> area = new ArrayList<>();
        for (final var raycast : raycasts) {
            final List<Vector> hits = new ArrayList<>();
            for (final var segment : segments) {
                final Vector intersectionPoint = segment.intersectionPoint(raycast);
                if (intersectionPoint != null) {
                    hits.add(intersectionPoint);
                }
            }
            Collections.sort(hits, new DistanceComparator(lightBox.position()));

            area.add(hits.isEmpty() ? raycast.to() : hits.get(0));
        }
        return area;
    }

    private List<Segment> getRelevantRaytraces(final Bounds source, final List<Bounds> colliders) {
        final var segments = new ArrayList<Segment>();
        segments.add(Segment.between(source.position(), source.bottomLeft()));
        segments.add(Segment.between(source.position(), source.bottomRight()));
        segments.add(Segment.between(source.position(), source.origin()));
        segments.add(Segment.between(source.position(), source.topRight()));
        final var range = source.height() / 2.0;
        Time now = Time.now();
        for (final var collider : colliders) {
            Vector position = source.position();
            final Segment normalTrace = Segment.between(position, position.addY(-range));

            segmentsOf(segments, position, normalTrace, collider.bottomLeft());
            segmentsOf(segments, position, normalTrace, collider.bottomRight());
            segmentsOf(segments, position, normalTrace, collider.origin());
            segmentsOf(segments, position, normalTrace, collider.topRight());
        }
        segments.sort(BY_ANGLE);
        System.out.println(Duration.since(now).nanos());
        return segments;
    }

    private void segmentsOf(final List<Segment> list, final Vector position, Segment normalTrace,
            final Vector destination) {
        final Segment directTrace = Segment.between(position, destination);
        final var rotationOfDirectTrace = Angle.of(directTrace).degrees();
        list.add(Angle.degrees(rotationOfDirectTrace - 0.01).rotate(normalTrace));
        list.add(directTrace);
        list.add(Angle.degrees(rotationOfDirectTrace + 0.01).rotate(normalTrace));
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

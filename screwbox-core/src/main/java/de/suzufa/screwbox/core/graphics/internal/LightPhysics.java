package de.suzufa.screwbox.core.graphics.internal;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.suzufa.screwbox.core.Angle;
import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Segment;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.physics.Borders;
import de.suzufa.screwbox.core.physics.internal.DistanceComparator;

public class LightPhysics {

    private List<Bounds> obstacles = new ArrayList<>();
    private final DefaultWorld world;

    public LightPhysics(final DefaultWorld world) {
        this.world = world;
    }

    public void setObstacles(final List<Bounds> obstacles) {
        this.obstacles = requireNonNull(obstacles, "obstacles must not be null");
    }

    public List<Bounds> obstacles() {
        return obstacles;
    }

    public List<Offset> calculateArea(final Bounds lightBox) {
        final var relevantBlockingBounds = lightBox.allIntersecting(obstacles);
        final List<Offset> area = new ArrayList<>();

        final List<Segment> raycasts = getRelevantRaytraces(lightBox, relevantBlockingBounds);
        for (final var raycast : raycasts) {
            final List<Vector> hits = new ArrayList<>();
            for (final var segment : getSegmentsOf(relevantBlockingBounds)) {
                final Vector intersectionPoint = segment.intersectionPoint(raycast);
                if (intersectionPoint != null) {
                    hits.add(intersectionPoint);
                }
            }
            Collections.sort(hits, new DistanceComparator(lightBox.position()));

            final Vector endpoint = hits.isEmpty() ? raycast.to() : hits.get(0);
            area.add(world.toOffset(endpoint));
        }
        return area;
    }

    private List<Segment> getRelevantRaytraces(final Bounds source, final List<Bounds> colliders) {
        var segments = new ArrayList<Segment>();
        segments.add(Segment.between(source.position(), source.bottomLeft()));
        segments.add(Segment.between(source.position(), source.bottomRight()));
        segments.add(Segment.between(source.position(), source.topLeft()));
        segments.add(Segment.between(source.position(), source.topRight()));
        final var range = source.extents().length();
        for (final var collider : colliders) {
            segments.addAll(segmentsOf(source, range, collider.bottomLeft()));
            segments.addAll(segmentsOf(source, range, collider.bottomRight()));
            segments.addAll(segmentsOf(source, range, collider.topLeft()));
            segments.addAll(segmentsOf(source, range, collider.topRight()));
        }
        segments.sort(new Comparator<Segment>() {

            @Override
            public int compare(final Segment o1, final Segment o2) {
                return Angle.of(o1).compareTo(Angle.of(o2));
            }
        });
        return segments;
    }

    private List<Segment> segmentsOf(final Bounds source, final double range, final Vector destination) {
        final Segment directTrace = Segment.between(source.position(), destination);
        final Segment normalTrace = Segment.between(source.position(), source.position().addY(-range));
        final var rotationOfDirectTrace = Angle.of(directTrace).degrees();
        return List.of(
                Angle.degrees(rotationOfDirectTrace - 0.01).rotate(normalTrace),
                directTrace,
                Angle.degrees(rotationOfDirectTrace + 0.01).rotate(normalTrace));
    }

    private List<Segment> getSegmentsOf(final List<Bounds> allBounds) {
        final List<Segment> allSegments = new ArrayList<>();
        for (final var bounds : allBounds) {
            allSegments.addAll(Borders.ALL.extractSegmentsMethod().apply(bounds));
        }
        return allSegments;
    }
}

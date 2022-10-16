package de.suzufa.screwbox.core.entities.systems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.suzufa.screwbox.core.Angle;
import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Segment;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.UpdatePriority;
import de.suzufa.screwbox.core.entities.components.LightBlockingComponent;
import de.suzufa.screwbox.core.entities.components.PointLightComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.graphics.Lightmap;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.physics.internal.DistanceComparator;

public class DynamicLightSystem implements EntitySystem {

    // TODO: What classes to see? / make lightmap part of the engine.graphics() /
    // async rendering of image
    private static final Archetype POINTLIGHT_EMITTERS = Archetype.of(
            PointLightComponent.class, TransformComponent.class);

    private static final Archetype LIGHT_BLOCKING = Archetype.of(
            LightBlockingComponent.class, TransformComponent.class);

    private final int resolution;

    public DynamicLightSystem() {
        this(6);
    }

    public DynamicLightSystem(final int resolution) {
        this.resolution = resolution;
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
                Angle.ofDegrees(rotationOfDirectTrace - 0.01).rotate(normalTrace),
                directTrace,
                Angle.ofDegrees(rotationOfDirectTrace + 0.01).rotate(normalTrace));
    }

    @Override
    public void update(final Engine engine) {
        final List<Entity> relevantLights = new ArrayList<>();
        for (final var light : engine.entities().fetchAll(POINTLIGHT_EMITTERS)) {
            final var lightRange = light.get(PointLightComponent.class).range;
            final var pointLightBounds = Bounds.atPosition(light.get(TransformComponent.class).bounds.position(),
                    lightRange,
                    lightRange);
            if (engine.graphics().world().visibleArea().intersects(pointLightBounds)) {
                relevantLights.add(light);
            }
        }
        final List<Bounds> lightBlockingBounds = new ArrayList<>();
        for (final var e : engine.entities().fetchAll(LIGHT_BLOCKING)) {
            lightBlockingBounds.add(e.get(TransformComponent.class).bounds);
        }

        final Lightmap lightmap = new Lightmap(engine.graphics().window().size(), resolution);
        for (final Entity pointLightEntity : relevantLights) {

            final PointLightComponent pointLight = pointLightEntity.get(PointLightComponent.class);
            final Vector pointLightPosition = pointLightEntity.get(TransformComponent.class).bounds.position();
            final Offset offset = engine.graphics().windowPositionOf(pointLightPosition);
            final var pointLightBounds = Bounds.atPosition(pointLightPosition,
                    pointLight.range,
                    pointLight.range);
            final var relevantBlockingBounds = getRelevantBlockingBounds(lightBlockingBounds, pointLightBounds);
            final List<Offset> area = new ArrayList<>();

            for (final var raycast : getRelevantRaytraces(pointLightBounds, relevantBlockingBounds)) {
                final List<Vector> hits = new ArrayList<>();
                for (final var s : getSegmentsOf(relevantBlockingBounds)) {
                    final Vector intersectionPoint = s.intersectionPoint(raycast);
                    if (intersectionPoint != null) {
                        hits.add(intersectionPoint);
                    }
                }
                Collections.sort(hits, new DistanceComparator(pointLightPosition));

                final Vector endpoint = hits.isEmpty() ? raycast.to() : hits.get(0);
                area.add(engine.graphics().windowPositionOf(endpoint));
            }
            lightmap.addPointLight(offset, (int) (pointLight.range * engine.graphics().cameraZoom()), area,
                    pointLight.color);
        }
        engine.graphics().window().drawLightmap(lightmap);
    }

    private List<Segment> getSegmentsOf(final List<Bounds> allBounds) {
        final List<Segment> allSegments = new ArrayList<>();
        for (final var bounds : allBounds) {
            allSegments.add(Segment.between(bounds.origin(), bounds.topRight()));
            allSegments.add(Segment.between(bounds.topRight(), bounds.bottomRight()));
            allSegments.add(Segment.between(bounds.bottomRight(), bounds.bottomLeft()));
            allSegments.add(Segment.between(bounds.bottomLeft(), bounds.origin()));
        }
        return allSegments;
    }

    private List<Bounds> getRelevantBlockingBounds(final List<Bounds> lightBlockingBounds,
            final Bounds pointLightBounds) {
        final List<Bounds> allIntersecting = new ArrayList<>();
        for (final var bounds : lightBlockingBounds) {
            if (pointLightBounds.intersects(bounds)) {
                allIntersecting.add(bounds);
            }
        }
        return allIntersecting;
    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PRESENTATION_LIGHT;
    }

}

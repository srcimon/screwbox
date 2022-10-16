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
import de.suzufa.screwbox.core.graphics.Color;
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

    private int resolution;

    public DynamicLightSystem() {
        this(6);
    }

    public DynamicLightSystem(int resolution) {
        this.resolution = resolution;
    }

    boolean intersects(List<Bounds> allLightBounds, Bounds bounds) {
        for (var lightBounds : allLightBounds) {
            if (lightBounds.intersects(bounds)) {
                return true;
            }
        }
        return false;
    }

    private List<Segment> getRelevantRaytraces(Bounds source, List<Bounds> colliders) {
        List<Segment> segments = new ArrayList<>();
        for (var collider : colliders) {
            if (collider.intersects(source)) {
                segments.addAll(segmentsOf(source, collider.bottomLeft()));
                segments.addAll(segmentsOf(source, collider.bottomRight()));
                segments.addAll(segmentsOf(source, collider.topLeft()));
                segments.addAll(segmentsOf(source, collider.topRight()));
            }
        }
        segments.sort(new Comparator<Segment>() {

            @Override
            public int compare(Segment o1, Segment o2) {
                return Angle.of(o1).compareTo(Angle.of(o2));
            }
        });
        return segments;
    }

    private List<Segment> segmentsOf(Bounds source, Vector destination) {
        Segment directTrace = Segment.between(source.position(), destination);
        return List.of(
//                Angle.ofDegrees(-0.1).rotate(directTrace),
                directTrace
//                Angle.ofDegrees(0.1).rotate(directTrace)
        );
    }

    @Override
    public void update(final Engine engine) {
        List<Entity> lightBlocking = engine.entities().fetchAll(LIGHT_BLOCKING);
        List<Entity> allLights = engine.entities().fetchAll(POINTLIGHT_EMITTERS);
        List<Entity> relevantLights = new ArrayList<>();

        List<Bounds> allLightBounds = new ArrayList<>();
        for (var light : allLights) {
            var lightRange = light.get(PointLightComponent.class).range;
            var pointLightBounds = Bounds.atPosition(light.get(TransformComponent.class).bounds.position(),
                    lightRange,
                    lightRange);
            if (engine.graphics().world().visibleArea().intersects(pointLightBounds)) {
                relevantLights.add(light);
            }
        }
        List<Bounds> lightBlockingBounds = new ArrayList<>();
        for (var e : lightBlocking) {
            lightBlockingBounds.add(e.get(TransformComponent.class).bounds);
        }
        for (var light : relevantLights) {
            var lightRange = light.get(PointLightComponent.class).range;
            var pointLightBounds = Bounds.atPosition(light.get(TransformComponent.class).bounds.position(),
                    lightRange,
                    lightRange);
            allLightBounds.add(pointLightBounds);
        }
        List<Segment> allSegments = new ArrayList<>();
        for (var blocking : lightBlocking) {
            TransformComponent transformComponent = blocking.get(TransformComponent.class);
            var bounds = transformComponent.bounds;
            if (intersects(allLightBounds, bounds)) {
                allSegments.add(Segment.between(bounds.origin(), bounds.topRight()));
                allSegments.add(Segment.between(bounds.topRight(), bounds.bottomRight()));
                allSegments.add(Segment.between(bounds.bottomRight(), bounds.bottomLeft()));
                allSegments.add(Segment.between(bounds.bottomLeft(), bounds.origin()));
            }
        }
        int colorNr = 0;

        try (final Lightmap lightmap = new Lightmap(engine.graphics().window().size(), resolution)) {

            for (final Entity pointLightEntity : allLights) {
                final PointLightComponent pointLight = pointLightEntity.get(PointLightComponent.class);
                final Vector pointLightPosition = pointLightEntity.get(TransformComponent.class).bounds.position();
                final Offset offset = engine.graphics().windowPositionOf(pointLightPosition);
                final int range = (int) pointLight.range;
                final List<Offset> area = new ArrayList<>();
                var pointLightBounds = Bounds.atPosition(pointLightPosition,
                        range,
                        range);

                for (var raycast : getRelevantRaytraces(pointLightBounds, lightBlockingBounds)) {
                    List<Vector> hits = new ArrayList<>();
                    for (var s : allSegments) {
                        Vector intersectionPoint = s.intersectionPoint(raycast);
                        if (intersectionPoint != null) {
                            hits.add(intersectionPoint);
                        }
                    }
                    Collections.sort(hits, new DistanceComparator(pointLightPosition));

                    Vector endpoint = hits.isEmpty() ? raycast.to() : hits.get(0);
                    area.add(engine.graphics().windowPositionOf(endpoint));
                    engine.graphics().world().drawCircle(endpoint, 2);
                }
                lightmap.addPointLight(offset, (int) (range * engine.graphics().cameraZoom()), area,
                        getColor(colorNr));
                colorNr++;
            }

            engine.graphics().window().drawLightmap(lightmap);
        }

    }

    private Color getColor(int colorNr) {
        List<Color> colors = List.of(Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN, Color.DARK_BLUE);
        return colors.get(Math.min(colors.size() - 1, colorNr));
    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PRESENTATION_LIGHT;
    }

}

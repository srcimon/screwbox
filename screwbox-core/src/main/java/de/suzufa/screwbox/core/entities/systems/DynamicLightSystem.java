package de.suzufa.screwbox.core.entities.systems;

import java.util.ArrayList;
import java.util.Collections;
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

    private final double raycastAngle;

    private int resolution;

    public DynamicLightSystem() {
        this(6, 7);
    }

    public DynamicLightSystem(final double raycastAngle, int resolution) {
        this.raycastAngle = raycastAngle;
        this.resolution = resolution;
    }

    @Override
    public void update(final Engine engine) {
        List<Entity> lightBlocking = engine.entities().fetchAll(LIGHT_BLOCKING);
        List<Segment> allSegments = new ArrayList<>();
        for (var blocking : lightBlocking) {
            TransformComponent transformComponent = blocking.get(TransformComponent.class);
            var bounds = transformComponent.bounds;
            allSegments.add(Segment.between(bounds.origin(), bounds.topRight()));
            allSegments.add(Segment.between(bounds.topRight(), bounds.bottomRight()));
            allSegments.add(Segment.between(bounds.bottomRight(), bounds.bottomLeft()));
            allSegments.add(Segment.between(bounds.bottomLeft(), bounds.origin()));

        }

        try (final Lightmap lightmap = new Lightmap(engine.graphics().window().size(), resolution)) {

            for (final Entity pointLightEntity : engine.entities().fetchAll(POINTLIGHT_EMITTERS)) {
                final PointLightComponent pointLight = pointLightEntity.get(PointLightComponent.class);
                final Vector pointLightPosition = pointLightEntity.get(TransformComponent.class).bounds.position();
                var pointLightBounds = Bounds.atPosition(pointLightPosition, pointLight.range, pointLight.range);
                final Offset offset = engine.graphics().windowPositionOf(pointLightPosition);
                final int range = (int) (pointLight.range / engine.graphics().cameraZoom());
                final List<Offset> area = new ArrayList<>();
                if (engine.graphics().world().visibleArea().intersects(pointLightBounds)) {
                    for (double degrees = 0; degrees < 360; degrees += raycastAngle) {
                        // TODO: make utility method in Angle for this:
                        double radians = Angle.ofDegrees(degrees).radians();
                        Vector raycastEnd = Vector.$(
                                pointLightPosition.x() + (range * Math.sin(radians)),
                                pointLightPosition.y() + (range * -Math.cos(radians)));

                        Segment raycast = Segment.between(pointLightPosition, raycastEnd);

                        List<Vector> hits = new ArrayList<>();
                        for (var s : allSegments) {
                            Vector intersectionPoint = s.intersectionPoint(raycast);
                            if (intersectionPoint != null) {
                                hits.add(intersectionPoint);
                            }
                        }

                        Collections.sort(hits, new DistanceComparator(pointLightPosition));

                        Vector endpoint = hits.isEmpty() ? raycastEnd : hits.get(0);
                        area.add(engine.graphics().windowPositionOf(endpoint));
                    }
                    lightmap.addPointLight(offset, range, area);

                }
            }
            engine.graphics().window().drawLightmap(lightmap);
        }
    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PRESENTATION_LIGHT;
    }

}

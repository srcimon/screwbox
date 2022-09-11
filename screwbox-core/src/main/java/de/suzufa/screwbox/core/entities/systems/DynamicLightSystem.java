package de.suzufa.screwbox.core.entities.systems;

import static java.util.Objects.isNull;

import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.UpdatePriority;
import de.suzufa.screwbox.core.entities.components.LightBlockingComponent;
import de.suzufa.screwbox.core.entities.components.LightEmitterComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.physics.internal.ShadowRequest;

public class DynamicLightSystem implements EntitySystem {

    private static final Archetype LIGHTS = Archetype.of(TransformComponent.class, LightEmitterComponent.class);
    private static final Archetype COLLIDERS = Archetype.of(TransformComponent.class, LightBlockingComponent.class);

    @Override
    public void update(Engine engine) {
        List<Entity> colliders = engine.entities().fetchAll(COLLIDERS);

        for (Entity entity : engine.entities().fetchAll(LIGHTS)) {
            Vector position = entity.get(TransformComponent.class).bounds.position();
            LightEmitterComponent lightEmitter = entity.get(LightEmitterComponent.class);
            double range = lightEmitter.range;
            for (var collider : colliders) {
                Bounds colliderBounds = collider.get(TransformComponent.class).bounds;
                Vector colliderPosition = colliderBounds.position();

                if (colliderPosition.distanceTo(position) <= range && !colliderBounds.contains(position)) {

                    boolean castShadow = isNull(lightEmitter.blockedBy) || !engine.physics().raycastFrom(position)
                            .ignoringEntities(entity, collider)
                            .checkingFor(lightEmitter.blockedBy)
                            .castingTo(colliderPosition)
                            .hasHit();

                    if (castShadow) {

                        var shadowRequest = new ShadowRequest(position,
                                colliderBounds.inflated(collider.get(LightBlockingComponent.class).sizeModifier
                                        * engine.graphics().cameraZoom()

                                ));
                        var distance = position.distanceTo(colliderPosition);
                        Percentage opacity = lightEmitter.fixedOpacity ? lightEmitter.shadowOpacity
                                : Percentage.of(lightEmitter.shadowOpacity.value() * ((range - distance) / range));
                        Color color = Color.BLACK.withOpacity(opacity);
                        engine.graphics().world().drawPolygon(shadowRequest.shadowPolygonPoints(), color);
                    }
                }
            }
        }

    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PRESENTATION_LIGHT;
    }

}

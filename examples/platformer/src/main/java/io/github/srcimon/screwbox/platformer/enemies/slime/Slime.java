package io.github.srcimon.screwbox.platformer.enemies.slime;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport.Converter;
import dev.screwbox.core.environment.ai.PatrolMovementComponent;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.light.GlowComponent;
import dev.screwbox.core.environment.light.PointLightComponent;
import dev.screwbox.core.environment.logic.StateComponent;
import dev.screwbox.core.environment.logic.TriggerAreaComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.FlipSpriteComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.platformer.components.CastShadowComponent;
import io.github.srcimon.screwbox.platformer.components.DeathEventComponent;
import io.github.srcimon.screwbox.platformer.components.KillZoneComponent;
import io.github.srcimon.screwbox.platformer.components.KilledFromAboveComponent;
import io.github.srcimon.screwbox.platformer.components.PlayerMarkerComponent;
import dev.screwbox.tiles.GameObject;

public class Slime implements Converter<GameObject> {

    @Override
    public Entity convert(final GameObject object) {
        return new Entity(object.id(), "Slime").add(
                new PointLightComponent(15, Color.BLACK.opacity(1)),
                new GlowComponent(45, Color.YELLOW.opacity(0.3)),
                new StateComponent(new SlimeAliveState()),
                new TransformComponent(Bounds.atPosition(object.position(), 12, 10)),
                new KillZoneComponent(DeathEventComponent.DeathType.ENEMY_TOUCHED),
                new CastShadowComponent(),
                new KilledFromAboveComponent(),
                new ColliderComponent(0, Percent.of(0.4)),
                new TriggerAreaComponent(Archetype.of(PlayerMarkerComponent.class)),
                new FlipSpriteComponent(),
                new PatrolMovementComponent(20),
                new PhysicsComponent(Vector.of(10, 0)),
                new RenderComponent(object.layer().order()));
    }

}

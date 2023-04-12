package io.github.srcimon.screwbox.examples.platformer.enemies.slime;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.entities.Archetype;
import io.github.srcimon.screwbox.core.entities.Entity;
import io.github.srcimon.screwbox.core.entities.SourceImport.Converter;
import io.github.srcimon.screwbox.core.entities.components.*;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.LightOptions;
import io.github.srcimon.screwbox.tiled.GameObject;
import io.github.srcimon.screwbox.examples.platformer.components.*;

public class Slime implements Converter<GameObject> {

    @Override
    public Entity convert(final GameObject object) {
        return new Entity(object.id()).add(
                new PointLightComponent(LightOptions.glowing(15)
                        .color(Color.BLACK.opacity(0.6))
                        .glow(2)
                        .glowColor(Color.YELLOW.opacity(0.4))),
                new StateComponent(new SlimeAliveState()),
                new TransformComponent(Bounds.atPosition(object.position(), 12, 10)),
                new KillZoneComponent(DeathEventComponent.DeathType.ENEMY_TOUCHED),
                new CastShadowComponent(),
                new KilledFromAboveComponent(),
                new ColliderComponent(0, Percent.of(0.4)),
                new TriggerAreaComponent(Archetype.of(PlayerMarkerComponent.class)),
                new SignalComponent(),
                new AutoFlipSpriteComponent(),
                new PatrollingMovementComponent(),
                new PhysicsBodyComponent(Vector.of(10, 0)),
                new RenderComponent(object.layer().order()));
    }

}

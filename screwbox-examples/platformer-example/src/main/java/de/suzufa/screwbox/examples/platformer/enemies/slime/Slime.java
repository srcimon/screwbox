package de.suzufa.screwbox.examples.platformer.enemies.slime;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Percent;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.SourceImport.Converter;
import de.suzufa.screwbox.core.entities.components.AutoFlipSpriteComponent;
import de.suzufa.screwbox.core.entities.components.ColliderComponent;
import de.suzufa.screwbox.core.entities.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entities.components.PointLightComponent;
import de.suzufa.screwbox.core.entities.components.SignalComponent;
import de.suzufa.screwbox.core.entities.components.RenderComponent;
import de.suzufa.screwbox.core.entities.components.StateComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.entities.components.TriggerAreaComponent;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.LightOptions;
import de.suzufa.screwbox.examples.platformer.components.CastShadowComponent;
import de.suzufa.screwbox.examples.platformer.components.KillZoneComponent;
import de.suzufa.screwbox.examples.platformer.components.KilledFromAboveComponent;
import de.suzufa.screwbox.examples.platformer.components.PatrollingMovementComponent;
import de.suzufa.screwbox.examples.platformer.components.PlayerMarkerComponent;
import de.suzufa.screwbox.examples.platformer.components.DeathEventComponent.DeathType;
import de.suzufa.screwbox.tiled.GameObject;

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
                new KillZoneComponent(DeathType.ENEMY_TOUCHED),
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

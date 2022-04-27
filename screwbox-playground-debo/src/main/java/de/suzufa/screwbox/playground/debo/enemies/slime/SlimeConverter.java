package de.suzufa.screwbox.playground.debo.enemies.slime;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.components.ColliderComponent;
import de.suzufa.screwbox.core.entityengine.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entityengine.components.SignalComponent;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.entityengine.components.StateComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.components.TriggerAreaComponent;
import de.suzufa.screwbox.playground.debo.components.AutoflipByMovementComponent;
import de.suzufa.screwbox.playground.debo.components.CastShadowComponent;
import de.suzufa.screwbox.playground.debo.components.DeathEventComponent.DeathType;
import de.suzufa.screwbox.playground.debo.components.KillZoneComponent;
import de.suzufa.screwbox.playground.debo.components.KilledFromAboveComponent;
import de.suzufa.screwbox.playground.debo.components.PatrollingMovementComponent;
import de.suzufa.screwbox.playground.debo.components.PlayerMarkerComponent;
import de.suzufa.screwbox.tiled.Converter;
import de.suzufa.screwbox.tiled.GameObject;

public class SlimeConverter implements Converter<GameObject> {

    @Override
    public boolean accepts(final GameObject object) {
        return "slime".equals(object.name());
    }

    @Override
    public Entity convert(final GameObject object) {
        return new Entity(object.id()).add(
                new StateComponent(new SlimeAliveState()),
                new TransformComponent(Bounds.atPosition(object.position(), 12, 10)),
                new KillZoneComponent(DeathType.ENEMY_TOUCHED),
                new CastShadowComponent(),
                new KilledFromAboveComponent(),
                new ColliderComponent(0, Percentage.of(0.4)),
                new TriggerAreaComponent(Archetype.of(PlayerMarkerComponent.class)),
                new SignalComponent(),
                new AutoflipByMovementComponent(),
                new PatrollingMovementComponent(),
                new PhysicsBodyComponent(Vector.of(10, 0)),
                new SpriteComponent(object.layer().order()));
    }

}

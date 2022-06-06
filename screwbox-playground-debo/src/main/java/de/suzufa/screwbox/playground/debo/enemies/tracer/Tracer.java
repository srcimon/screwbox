package de.suzufa.screwbox.playground.debo.enemies.tracer;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.components.SignalComponent;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.entityengine.components.StateComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.components.TriggerAreaComponent;
import de.suzufa.screwbox.core.resources.EntityBuilder.Converter;
import de.suzufa.screwbox.playground.debo.components.DeathEventComponent.DeathType;
import de.suzufa.screwbox.playground.debo.components.DetectLineOfSightToPlayerComponent;
import de.suzufa.screwbox.playground.debo.components.KillZoneComponent;
import de.suzufa.screwbox.playground.debo.components.PlayerMarkerComponent;
import de.suzufa.screwbox.tiled.GameObject;

public class Tracer implements Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        return new Entity(object.id()).add(
                new TransformComponent(Bounds.atPosition(object.position(), 16, 16)),
                new SpriteComponent(object.layer().order()),
                new TriggerAreaComponent(Archetype.of(PlayerMarkerComponent.class, TransformComponent.class)),
                new SignalComponent(),
                new DetectLineOfSightToPlayerComponent(140),
                new KillZoneComponent(DeathType.SPIKES),
                new StateComponent(new TracerInactiveState()));
    }

}

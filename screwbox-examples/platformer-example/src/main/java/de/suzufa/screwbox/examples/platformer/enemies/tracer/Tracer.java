package de.suzufa.screwbox.examples.platformer.enemies.tracer;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.SourceImport.Converter;
import de.suzufa.screwbox.core.entities.components.SignalComponent;
import de.suzufa.screwbox.core.entities.components.SpriteComponent;
import de.suzufa.screwbox.core.entities.components.StateComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.entities.components.TriggerAreaComponent;
import de.suzufa.screwbox.examples.platformer.components.DetectLineOfSightToPlayerComponent;
import de.suzufa.screwbox.examples.platformer.components.KillZoneComponent;
import de.suzufa.screwbox.examples.platformer.components.PlayerMarkerComponent;
import de.suzufa.screwbox.examples.platformer.components.DeathEventComponent.DeathType;
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

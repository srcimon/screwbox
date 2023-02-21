package io.github.simonbas.screwbox.examples.platformer.enemies.tracer;

import io.github.simonbas.screwbox.core.Bounds;
import io.github.simonbas.screwbox.core.entities.Archetype;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.SourceImport.Converter;
import io.github.simonbas.screwbox.core.entities.components.*;
import io.github.simonbas.screwbox.examples.platformer.components.DeathEventComponent;
import io.github.simonbas.screwbox.examples.platformer.components.DetectLineOfSightToPlayerComponent;
import io.github.simonbas.screwbox.examples.platformer.components.KillZoneComponent;
import io.github.simonbas.screwbox.examples.platformer.components.PlayerMarkerComponent;
import io.github.simonbas.screwbox.tiled.GameObject;

public class Tracer implements Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        return new Entity(object.id()).add(
                new TransformComponent(Bounds.atPosition(object.position(), 16, 16)),
                new RenderComponent(object.layer().order()),
                new TriggerAreaComponent(Archetype.of(PlayerMarkerComponent.class, TransformComponent.class)),
                new SignalComponent(),
                new DetectLineOfSightToPlayerComponent(140),
                new KillZoneComponent(DeathEventComponent.DeathType.SPIKES),
                new StateComponent(new TracerInactiveState()));
    }

}

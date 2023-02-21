package io.github.simonbas.screwbox.examples.platformer.zones;

import io.github.simonbas.screwbox.core.entities.Archetype;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.SourceImport.Converter;
import io.github.simonbas.screwbox.core.entities.components.SignalComponent;
import io.github.simonbas.screwbox.core.entities.components.TransformComponent;
import io.github.simonbas.screwbox.core.entities.components.TriggerAreaComponent;
import io.github.simonbas.screwbox.examples.platformer.components.DeathEventComponent.DeathType;
import io.github.simonbas.screwbox.examples.platformer.components.KillZoneComponent;
import io.github.simonbas.screwbox.examples.platformer.components.PlayerMarkerComponent;
import io.github.simonbas.screwbox.tiled.GameObject;

public class KillZone implements Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        String deathType = object.properties().get("death-type").orElse("SPIKES");

        return new Entity().add(
                new SignalComponent(),
                new TriggerAreaComponent(Archetype.of(PlayerMarkerComponent.class, TransformComponent.class)),
                new KillZoneComponent(DeathType.valueOf(deathType)),
                new TransformComponent(object.bounds()));
    }

}

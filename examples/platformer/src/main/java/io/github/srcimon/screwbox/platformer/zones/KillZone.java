package io.github.srcimon.screwbox.platformer.zones;

import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport.Converter;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.logic.SignalComponent;
import io.github.srcimon.screwbox.core.environment.logic.TriggerAreaComponent;
import io.github.srcimon.screwbox.platformer.components.DeathEventComponent.DeathType;
import io.github.srcimon.screwbox.platformer.components.KillZoneComponent;
import io.github.srcimon.screwbox.platformer.components.PlayerMarkerComponent;
import io.github.srcimon.screwbox.tiled.GameObject;

public class KillZone implements Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        String deathType = object.properties().tryGetString("death-type").orElse("SPIKES");

        return new Entity().add(
                new SignalComponent(),
                new TriggerAreaComponent(Archetype.ofSpacial(PlayerMarkerComponent.class)),
                new KillZoneComponent(DeathType.valueOf(deathType)),
                new TransformComponent(object.bounds()));
    }

}

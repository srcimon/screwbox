package io.github.srcimon.screwbox.platformer.zones;

import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport.Converter;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.logic.TriggerAreaComponent;
import io.github.srcimon.screwbox.platformer.components.DeathEventComponent.DeathType;
import io.github.srcimon.screwbox.platformer.components.KillZoneComponent;
import io.github.srcimon.screwbox.platformer.components.PlayerMarkerComponent;
import dev.screwbox.tiles.GameObject;

public class KillZone implements Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        var deathType = object.properties().tryGetEnum("death-type", DeathType.class).orElse(DeathType.SPIKES);

        return new Entity().add(
                new TriggerAreaComponent(Archetype.ofSpacial(PlayerMarkerComponent.class)),
                new KillZoneComponent(deathType),
                new TransformComponent(object.bounds()));
    }

}

package io.github.srcimon.screwbox.examples.platformer.zones;

import io.github.srcimon.screwbox.core.entities.Archetype;
import io.github.srcimon.screwbox.core.entities.Entity;
import io.github.srcimon.screwbox.core.entities.SourceImport.Converter;
import io.github.srcimon.screwbox.core.entities.components.SignalComponent;
import io.github.srcimon.screwbox.core.entities.components.TransformComponent;
import io.github.srcimon.screwbox.core.entities.components.TriggerAreaComponent;
import io.github.srcimon.screwbox.examples.platformer.components.ChangeMapComponent;
import io.github.srcimon.screwbox.examples.platformer.components.PlayerMarkerComponent;
import io.github.srcimon.screwbox.tiled.GameObject;

public class ChangeMapZone implements Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        return new Entity().add(
                new SignalComponent(),
                new TriggerAreaComponent(Archetype.of(PlayerMarkerComponent.class, TransformComponent.class)),
                new ChangeMapComponent(object.properties().force("file-name")),
                new TransformComponent(object.bounds()));
    }

}

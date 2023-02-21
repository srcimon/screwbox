package io.github.simonbas.screwbox.examples.platformer.zones;

import io.github.simonbas.screwbox.core.entities.Archetype;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.SourceImport.Converter;
import io.github.simonbas.screwbox.core.entities.components.SignalComponent;
import io.github.simonbas.screwbox.core.entities.components.TransformComponent;
import io.github.simonbas.screwbox.core.entities.components.TriggerAreaComponent;
import io.github.simonbas.screwbox.examples.platformer.components.ChangeMapComponent;
import io.github.simonbas.screwbox.examples.platformer.components.PlayerMarkerComponent;
import io.github.simonbas.screwbox.tiled.GameObject;

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

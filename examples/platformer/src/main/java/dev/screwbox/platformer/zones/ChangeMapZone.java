package dev.screwbox.platformer.zones;

import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport.Converter;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.logic.TriggerAreaComponent;
import dev.screwbox.platformer.components.ChangeMapComponent;
import dev.screwbox.platformer.components.PlayerMarkerComponent;
import dev.screwbox.tiled.GameObject;

public class ChangeMapZone implements Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        return new Entity().add(
                new TriggerAreaComponent(Archetype.ofSpacial(PlayerMarkerComponent.class)),
                new ChangeMapComponent(object.properties().getString("file-name")),
                new TransformComponent(object.bounds()));
    }

}

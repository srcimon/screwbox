package io.github.srcimon.screwbox.platformer.zones;

import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport.Converter;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.logic.SignalComponent;
import io.github.srcimon.screwbox.core.environment.logic.TriggerAreaComponent;
import io.github.srcimon.screwbox.platformer.components.ChangeMapComponent;
import io.github.srcimon.screwbox.platformer.components.PlayerMarkerComponent;
import io.github.srcimon.screwbox.tiled.GameObject;

public class ChangeMapZone implements Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        return new Entity().add(
                new SignalComponent(),
                new TriggerAreaComponent(Archetype.ofSpacial(PlayerMarkerComponent.class)),
                new ChangeMapComponent(object.properties().getString("file-name")),
                new TransformComponent(object.bounds()));
    }

}

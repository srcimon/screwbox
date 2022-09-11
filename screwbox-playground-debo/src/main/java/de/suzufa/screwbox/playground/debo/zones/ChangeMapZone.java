package de.suzufa.screwbox.playground.debo.zones;

import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.SourceImport.Converter;
import de.suzufa.screwbox.core.entities.components.SignalComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.entities.components.TriggerAreaComponent;
import de.suzufa.screwbox.playground.debo.components.ChangeMapComponent;
import de.suzufa.screwbox.playground.debo.components.PlayerMarkerComponent;
import de.suzufa.screwbox.tiled.GameObject;

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

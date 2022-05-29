package de.suzufa.screwbox.playground.debo.zones;

import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.components.SignalComponent;
import de.suzufa.screwbox.core.entityengine.components.TriggerAreaComponent;
import de.suzufa.screwbox.core.resources.EntityConverter;
import de.suzufa.screwbox.playground.debo.components.ChangeMapComponent;
import de.suzufa.screwbox.playground.debo.components.PlayerMarkerComponent;
import de.suzufa.screwbox.tiled.GameObject;

public class ChangeMapZoneConverter implements EntityConverter<GameObject> {

    @Override
    public boolean accepts(GameObject object) {
        return "change-map-zone".equals(object.name());
    }

    @Override
    public Entity convert(GameObject object) {
        return new Entity().add(
                new SignalComponent(),
                new TriggerAreaComponent(Archetype.of(PlayerMarkerComponent.class, TransformComponent.class)),
                new ChangeMapComponent(object.properties().force("file-name")),
                new TransformComponent(object.bounds()));
    }

}

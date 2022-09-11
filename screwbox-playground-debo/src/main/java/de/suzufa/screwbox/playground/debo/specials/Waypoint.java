package de.suzufa.screwbox.playground.debo.specials;

import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.SourceImport.Converter;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.playground.debo.components.WaypointComponent;
import de.suzufa.screwbox.tiled.GameObject;

public class Waypoint implements Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        return new Entity(object.id()).add(
                new TransformComponent(object.bounds()),
                new WaypointComponent(object.properties().forceInt("next")));
    }

}

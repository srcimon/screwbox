package io.github.srcimon.screwbox.examples.platformer.specials;

import io.github.srcimon.screwbox.core.entities.Entity;
import io.github.srcimon.screwbox.core.entities.SourceImport.Converter;
import io.github.srcimon.screwbox.core.entities.components.TransformComponent;
import io.github.srcimon.screwbox.examples.platformer.components.WaypointComponent;
import io.github.srcimon.screwbox.tiled.GameObject;

public class Waypoint implements Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        return new Entity(object.id()).add(
                new TransformComponent(object.bounds()),
                new WaypointComponent(object.properties().forceInt("next")));
    }

}

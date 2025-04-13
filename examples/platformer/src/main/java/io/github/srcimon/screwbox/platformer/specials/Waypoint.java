package io.github.srcimon.screwbox.platformer.specials;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport.Converter;
import dev.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.platformer.components.WaypointComponent;
import dev.screwbox.tiles.GameObject;

public class Waypoint implements Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        return new Entity(object.id()).add(
                new TransformComponent(object.bounds()),
                new WaypointComponent(object.properties().getInt("next")));
    }

}

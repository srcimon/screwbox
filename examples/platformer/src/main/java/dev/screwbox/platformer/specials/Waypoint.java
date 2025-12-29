package dev.screwbox.platformer.specials;

import dev.screwbox.core.environment.Blueprint;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.platformer.components.WaypointComponent;
import dev.screwbox.tiled.GameObject;

public class Waypoint implements Blueprint<GameObject> {

    @Override
    public Entity assembleFrom(GameObject object) {
        return new Entity(object.id()).add(
                new TransformComponent(object.bounds()),
                new WaypointComponent(object.properties().getInt("next")));
    }

}

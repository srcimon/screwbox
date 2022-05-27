package de.suzufa.screwbox.playground.debo.specials;

import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.resources.Converter;
import de.suzufa.screwbox.playground.debo.components.WaypointComponent;
import de.suzufa.screwbox.tiled.GameObject;

public class WaypointConverter implements Converter<GameObject> {

    @Override
    public boolean accepts(GameObject object) {
        return "waypoint".equals(object.name());
    }

    @Override
    public Entity convert(GameObject object) {
        return new Entity(object.id()).add(
                new TransformComponent(object.bounds()),
                new WaypointComponent(object.properties().forceInt("next")));
    }

}

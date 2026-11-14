package dev.screwbox.playground.gamescene.mapimport;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.importing.Blueprint;
import dev.screwbox.playground.gamescene.WaypointComponent;
import dev.screwbox.playground.gamescene.flag.FlagPointComponent;
import dev.screwbox.tiled.GameObject;

public class Waypoint implements Blueprint<GameObject> {
    @Override
    public Entity assembleFrom(GameObject source) {
        return new Entity()
            .bounds(source.bounds())
            .add(new WaypointComponent());
    }
}

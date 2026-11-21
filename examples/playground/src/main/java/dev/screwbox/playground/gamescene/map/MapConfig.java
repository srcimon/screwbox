package dev.screwbox.playground.gamescene.map;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.importing.Blueprint;
import dev.screwbox.core.environment.navigation.NavigationRegionComponent;
import dev.screwbox.core.environment.rendering.CameraBoundsComponent;
import dev.screwbox.playground.gamescene.teams.respawn.RespawnConfigComponent;
import dev.screwbox.tiled.Map;

public class MapConfig implements Blueprint<Map> {

    @Override
    public Entity assembleFrom(Map map) {
        return new Entity()
            .bounds(map.bounds())
            .add(new RespawnConfigComponent())
            .add(new CameraBoundsComponent())
            .add(new NavigationRegionComponent());
    }
}

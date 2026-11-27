package dev.screwbox.playground.game.blueprints;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.importing.Blueprint;
import dev.screwbox.playground.game.teams.respawn.RespawnAreaComponent;
import dev.screwbox.tiled.GameObject;

public class RespawnArea implements Blueprint<GameObject> {

    @Override
    public Entity assembleFrom(GameObject object) {
        return new Entity(object.id())
            .bounds(object.bounds())
            .add(new RespawnAreaComponent(), config -> config.team = object.properties().getInt("team-id"));
    }
}

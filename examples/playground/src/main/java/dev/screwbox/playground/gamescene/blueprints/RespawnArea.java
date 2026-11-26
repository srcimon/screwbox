package dev.screwbox.playground.gamescene.blueprints;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.importing.Blueprint;
import dev.screwbox.playground.TeamInfos;
import dev.screwbox.playground.gamescene.teams.respawn.RespawnAreaComponent;
import dev.screwbox.tiled.GameObject;

public record RespawnArea(TeamInfos teamInfos) implements Blueprint<GameObject> {
    @Override
    public Entity assembleFrom(GameObject object) {
        return new Entity(object.id())
            .bounds(object.bounds())
            .add(new RespawnAreaComponent(), config -> {
                config.team = teamInfos.teamId(object.properties().getInt("team"));
            });
    }
}

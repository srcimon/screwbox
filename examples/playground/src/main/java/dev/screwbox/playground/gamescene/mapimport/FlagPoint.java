package dev.screwbox.playground.gamescene.mapimport;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.importing.Blueprint;
import dev.screwbox.playground.gamescene.gamemodes.ctf.FlagPointComponent;
import dev.screwbox.tiled.GameObject;

public class FlagPoint implements Blueprint<GameObject> {
    @Override
    public Entity assembleFrom(GameObject source) {
        return new Entity(source.id())
            .bounds(source.bounds())
            .add(new FlagPointComponent(), config -> config.team = source.properties().getColor("team"));
    }
}

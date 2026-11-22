package dev.screwbox.playground.gamescene.blueprints;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.importing.Blueprint;
import dev.screwbox.core.environment.importing.ComplexBlueprint;
import dev.screwbox.core.environment.importing.IdPool;
import dev.screwbox.playground.gamescene.gamemodes.ctf.FlagPointComponent;
import dev.screwbox.tiled.GameObject;

import java.util.List;

public class FlagPoint implements ComplexBlueprint<GameObject> {

    @Override
    public List<Entity> assembleFrom(GameObject source, IdPool idPool) {
        final var flagPoint = new Entity(source.id())
            .bounds(source.bounds())
            .add(new FlagPointComponent(), config -> config.team = source.properties().getColor("team"));

        return List.of(flagPoint);
    }
}

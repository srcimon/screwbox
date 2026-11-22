package dev.screwbox.playground.gamescene.blueprints;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.importing.AdvancedBlueprint;
import dev.screwbox.core.environment.importing.IdPool;
import dev.screwbox.core.environment.light.SpotLightComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.playground.gamescene.gamemodes.ctf.FlagComponent;
import dev.screwbox.tiled.GameObject;

public class Flag implements AdvancedBlueprint<GameObject> {

    @Override
    public Entity assembleFrom(GameObject source, IdPool idPool) {
        final Color team = source.properties().getColor("team");

        return new Entity(idPool.allocateId())
            .bounds(source.bounds())
            .add(new SpotLightComponent(40, team.opacity(0.5)))
            .add(new RenderComponent(Sprite.placeholder(team, Size.square(8)), source.layer().order()))
            .add(new FlagComponent(), flag -> {
                flag.homeZoneId = source.id();
                flag.team = team;
            });
    }
}

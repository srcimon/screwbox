package dev.screwbox.playground.game.blueprints;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.importing.AdvancedBlueprint;
import dev.screwbox.core.environment.importing.IdPool;
import dev.screwbox.core.environment.light.SpotLightComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.playground.game.gamemodes.ctf.FlagComponent;
import dev.screwbox.tiled.GameObject;

public class Flag implements AdvancedBlueprint<GameObject> {


    @Override
    public Entity assembleFrom(GameObject source, IdPool idPool) {
        final var teamId = source.properties().getInt("team-id");
        final var teamColor = source.properties().getColor("team-color");
        return new Entity(idPool.allocateId())
            .bounds(source.bounds())
            .add(new SpotLightComponent(40, teamColor.opacity(0.5)))
            .add(new RenderComponent(Sprite.placeholder(teamColor, Size.square(8)), source.layer().order()), config -> config.isSortOrthographic = true)
            .add(new FlagComponent(), flag -> {
                flag.homeZoneId = source.id();
                flag.teamId = teamId;
            });
    }
}

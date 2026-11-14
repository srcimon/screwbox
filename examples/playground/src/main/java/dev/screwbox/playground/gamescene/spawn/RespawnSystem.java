package dev.screwbox.playground.gamescene.spawn;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Time;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.utils.ListUtil;
import dev.screwbox.playground.gamescene.player.PlayerComponent;

public class RespawnSystem implements EntitySystem {

    private static final Archetype PLAYERS = Archetype.of(PlayerComponent.class);
    private static final Archetype RESPAWN_AREAS = Archetype.ofSpacial(RespawnAreaComponent.class);
    private static final Archetype TEAMS = Archetype.of(TeamConfigComponent.class);

    @Override
    public void update(Engine engine) {
        final var respawnConfig = engine.environment().fetchSingletonComponent(RespawnConfigComponent.class);
        final var players = engine.environment().fetchAll(PLAYERS);
        final var respawnAreas = engine.environment().fetchAll(RESPAWN_AREAS);
        for (var team : engine.environment().fetchAll(TEAMS)) {
            final var teamConfig = team.get(TeamConfigComponent.class);


        }
    }
}

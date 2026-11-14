package dev.screwbox.playground.gamescene.spawn;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
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
            final var playerCount = players.stream().filter(p -> p.get(PlayerComponent.class).team.equals(teamConfig.teamColor)).count();
            if (playerCount < teamConfig.teamSize && respawnConfig.respawnTimeout.addTo(teamConfig.lastRespawn).isBefore(engine.loop().time())) {
                final var availableAreas = respawnAreas.stream().filter(a -> a.get(RespawnAreaComponent.class).team.equals(teamConfig.teamColor)).toList();
                final var area = ListUtil.pickRandom(availableAreas);
                Entity player = new Entity()
                    .bounds(Bounds.atPosition(area.bounds().randomPosition(), 16, 16))
                    .add(new RenderComponent(Sprite.placeholder(Color.ORANGE, Size.square(16))))
                    .add(new PlayerComponent(), p -> p.team = teamConfig.teamColor);
                teamConfig.lastRespawn = engine.loop().time();
                engine.environment().addEntity(player);
            }
        }
    }
}

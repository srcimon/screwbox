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

    @Override
    public void update(Engine engine) {
        final var respawnConfig = engine.environment().fetchSingletonComponent(RespawnConfigComponent.class);
        final var players = engine.environment().fetchAll(PLAYERS);
        final var respawnAreas = engine.environment().fetchAll(RESPAWN_AREAS);
        for (var team : respawnConfig.teams) {
            final var teamCount = players.stream().filter(p -> p.get(PlayerComponent.class).team.equals(team)).count();
            for (int i = 0; i < (respawnConfig.teams.size() - teamCount); i++) {
                var availableAreas = respawnAreas.stream().filter(a -> a.get(RespawnAreaComponent.class).team.equals(team)).toList();
                var area = ListUtil.pickRandom(availableAreas);
                Entity player = new Entity()
                    .bounds(Bounds.atPosition(area.bounds().randomPosition(), 16, 16))
                    .add(new RenderComponent(Sprite.placeholder(Color.ORANGE, Size.square(16))))
                    .add(new PlayerComponent(), p -> p.team = team);
                engine.environment().addEntity(player);
            }
        }
    }
}

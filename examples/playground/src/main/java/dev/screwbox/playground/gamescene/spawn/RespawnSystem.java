package dev.screwbox.playground.gamescene.spawn;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
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
        for (final var player : players) {
            var playerConfig = player.get(PlayerComponent.class);
            if (playerConfig.isDead && respawnConfig.respawnTimeout.addTo(playerConfig.lastRespawn).isBefore(engine.loop().time())) {
                var availableAreas = respawnAreas.stream().filter(area -> area.get(RespawnAreaComponent.class).team.equals(playerConfig.team)).toList();
                player.moveTo(ListUtil.pickRandom(availableAreas).bounds().randomPosition());
                playerConfig.isDead = false;
                playerConfig.lastRespawn = engine.loop().time();
            }
        }
    }

}

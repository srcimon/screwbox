package dev.screwbox.playground.gamescene.team.respawn;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.utils.ListUtil;
import dev.screwbox.playground.gamescene.team.health.HealthComponent;
import dev.screwbox.playground.gamescene.team.HeavenComponent;
import dev.screwbox.playground.gamescene.team.TeamMemberComponent;

import java.util.ArrayList;
import java.util.List;

public class RespawnSystem implements EntitySystem {

    private static final Archetype RESPAWN_AREAS = Archetype.ofSpacial(RespawnAreaComponent.class);

    @Override
    public void update(Engine engine) {
        final var respawnConfig = engine.environment().fetchSingletonComponent(RespawnConfigComponent.class);

        final var respawnAreas = engine.environment().fetchAll(RESPAWN_AREAS);
        HeavenComponent heaven = engine.environment().fetchSingletonComponent(HeavenComponent.class);
        final List<Entity> revivedPlayer = new ArrayList<>();
        for (final var player : heaven.deadPlayers) {
            var config = player.get(TeamMemberComponent.class);
            if (respawnConfig.respawnTimeout.addTo(config.deathTime).isBefore(engine.loop().time())) {
                var availableAreas = respawnAreas.stream().filter(area -> area.get(RespawnAreaComponent.class).team.equals(config.team)).toList();
                player.moveTo(ListUtil.pickRandom(availableAreas).bounds().randomPosition());
                engine.environment().addEntity(player);
                player.get(HealthComponent.class).health = Percent.max();
                revivedPlayer.add(player);
            }
        }
        heaven.deadPlayers.removeAll(revivedPlayer);
    }

}

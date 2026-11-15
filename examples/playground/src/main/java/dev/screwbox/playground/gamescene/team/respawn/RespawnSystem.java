package dev.screwbox.playground.gamescene.team.respawn;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.utils.ListUtil;
import dev.screwbox.playground.gamescene.team.TeamMemberComponent;

public class RespawnSystem implements EntitySystem {

    private static final Archetype TEAM_MEMBERS = Archetype.of(TeamMemberComponent.class);
    private static final Archetype RESPAWN_AREAS = Archetype.ofSpacial(RespawnAreaComponent.class);

    @Override
    public void update(Engine engine) {
        final var respawnConfig = engine.environment().fetchSingletonComponent(RespawnConfigComponent.class);

        final var respawnAreas = engine.environment().fetchAll(RESPAWN_AREAS);
        for (final var teamMember : engine.environment().fetchAll(TEAM_MEMBERS)) {
            var config = teamMember.get(TeamMemberComponent.class);
            if (config.isDead && respawnConfig.respawnTimeout.addTo(config.lastRespawn).isBefore(engine.loop().time())) {
                var availableAreas = respawnAreas.stream().filter(area -> area.get(RespawnAreaComponent.class).team.equals(config.team)).toList();
                teamMember.moveTo(ListUtil.pickRandom(availableAreas).bounds().randomPosition());
                config.isDead = false;
                config.lastRespawn = engine.loop().time();
            }
        }
    }

}

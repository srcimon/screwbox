package dev.screwbox.playground.gamescene.teams.health;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.playground.gamescene.teams.HeavenComponent;
import dev.screwbox.playground.gamescene.teams.TeamMemberComponent;

public class HealthSystem implements EntitySystem {

    private static final Archetype LIVING = Archetype.of(HealthComponent.class);

    @Override
    public void update(Engine engine) {
        for(final var living : engine.environment().fetchAll(LIVING)) {
            if(living.get(HealthComponent.class).health.isZero()) {
                living.get(TeamMemberComponent.class).deathTime = engine.loop().time();
                engine.environment().fetchSingletonComponent(HeavenComponent.class).deadPlayers.add(living);
                engine.environment().remove(living);

            }
        }
    }
}

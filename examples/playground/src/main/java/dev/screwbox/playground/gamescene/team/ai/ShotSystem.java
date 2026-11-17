package dev.screwbox.playground.gamescene.team.ai;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.playground.gamescene.team.health.HealthComponent;
import dev.screwbox.playground.gamescene.team.TeamMemberComponent;

public class ShotSystem implements EntitySystem {
    private static final Archetype SHOTS = Archetype.ofSpacial(ShotComponent.class);
    private static final Archetype TARGETS = Archetype.ofSpacial(TeamMemberComponent.class);

    @Override
    public void update(Engine engine) {
        final var shots = engine.environment().fetchAll(SHOTS);
        final var targets = engine.environment().fetchAll(TARGETS);
        for(final var shot : shots) {
            ShotComponent shotComponent = shot.get(ShotComponent.class);
            var invulnerableTeam = shotComponent.team;

            for(final var target : targets) {
                if(shot.bounds().intersects(target.bounds())) {
                    if(target.get(TeamMemberComponent.class).team != invulnerableTeam) {
                        var healthComponent = target.get(HealthComponent.class);
                        healthComponent.health = healthComponent.health.subtract(shotComponent.damage.value());
                        engine.environment().remove(shot);
                    }
                }
            }
        }
    }
}

package dev.screwbox.playground.game.teams.ai;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.playground.game.map.ShotObstacleComponent;
import dev.screwbox.playground.game.teams.PlayerComponent;
import dev.screwbox.playground.game.teams.health.HealthComponent;

public class ShotSystem implements EntitySystem {
    private static final Archetype SHOTS = Archetype.ofSpacial(ShotComponent.class);
    private static final Archetype TARGETS = Archetype.ofSpacial(PlayerComponent.class);
    private static final Archetype OBSTACLES = Archetype.ofSpacial(ShotObstacleComponent.class);

    @Override
    public void update(Engine engine) {
        final var shots = engine.environment().fetchAll(SHOTS);
        for (final var shot : shots) {
            ShotComponent shotComponent = shot.get(ShotComponent.class);
            var invulnerableTeam = shotComponent.teamId;
            for (final var target : engine.environment().fetchAll(TARGETS)) {
                if (shot.intersects(target)) {
                    if (target.get(PlayerComponent.class).teamId != invulnerableTeam) {
                        var healthComponent = target.get(HealthComponent.class);
                        healthComponent.health = healthComponent.health.subtract(shotComponent.damage.value());

                        engine.environment().remove(shot);
                    }
                }
            }
            for (final var collider : engine.environment().fetchAll(OBSTACLES)) {
                if (collider.intersects(shot)) {
                    engine.environment().remove(shot);
                }
            }
        }
    }
}

package dev.screwbox.playground.gamescene.teams.ai;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.playground.gamescene.GameFunctions;
import dev.screwbox.playground.gamescene.teams.TeamMemberComponent;

public class AttackSystem implements EntitySystem {

    private static final Archetype ATTACKERS = Archetype.ofSpacial(AttackComponent.class, TeamMemberComponent.class);
    private static final Archetype TARGETS = Archetype.ofSpacial(TeamMemberComponent.class, PhysicsComponent.class);

    @Override
    public void update(Engine engine) {
        final var attackers = engine.environment().fetchAll(ATTACKERS);
        final var targets = engine.environment().fetchAll(TARGETS);

        for (final var attacker : attackers) {
            var config = attacker.get(AttackComponent.class);
            var memberConfig = attacker.get(TeamMemberComponent.class);
            for (final var target : targets.stream().filter(target -> target.get(TeamMemberComponent.class).team != memberConfig.team).toList()) {
                if (attacker.position().distanceTo(target.position()) < config.range) {
                    final var leadPoistion = target.position().add(target.get(PhysicsComponent.class).velocity.multiply(0.5));
                    boolean isFree = engine.navigation().raycastFrom(attacker.position())
                        .checkingFor(Archetype.ofSpacial(ColliderComponent.class))
                        .ignoringEntities(target, attacker)
                        .castingTo(leadPoistion)
                        .noHit();
                    if (isFree) {
                        if (config.scheduler.isTick(engine.loop().time())) {
                            Vector velocity = leadPoistion.subtract(attacker.position()).length(120);
                            Vector position = attacker.position();
                            Color team = memberConfig.team;
                            GameFunctions.triggerShot(engine, team, velocity, position);
                        }
                    }
                }
            }

        }
    }

}

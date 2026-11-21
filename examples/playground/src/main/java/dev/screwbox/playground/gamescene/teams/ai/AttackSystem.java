package dev.screwbox.playground.gamescene.teams.ai;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.audio.SoundBundle;
import dev.screwbox.core.audio.SoundOptions;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
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
                if (attacker.position().distanceTo(target.position()) < config.range) {//TODO configure
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
                            triggerShot(engine, team, velocity, position);
                        }
                    }
                }
            }

        }
    }

    //TODO plugin candidate
    private static void triggerShot(Engine engine, Color team, Vector velocity, Vector position) {
        engine.audio().playSound(SoundBundle.PHASER, SoundOptions.playOnce().position(position));
        engine.environment().addEntity(new Entity(engine.environment().allocateId())
            .add(new ShotComponent(), shot -> shot.team = team)
            .add(new RenderComponent(SpriteBundle.DOT_YELLOW, SpriteDrawOptions.scaled(0.25)))
            .add(new PhysicsComponent(velocity), p -> p.ignoreCollisions = true)
            .add(new TransformComponent(Bounds.atPosition(position, 4, 4)))
        );
    }
}

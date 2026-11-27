package dev.screwbox.playground.game.teams;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.playground.game.GameFunctions;
import dev.screwbox.playground.game.teams.ai.AttackComponent;

public class PlayerAttackSystem implements EntitySystem {

    private static final Archetype ATTACKERS = Archetype.ofSpacial(HumanComponent.class);


    @Override
    public void update(Engine engine) {
        for (final var attacker : engine.environment().fetchAll(ATTACKERS)) {
            var config = attacker.get(AttackComponent.class);
            var memberConfig = attacker.get(PlayerComponent.class);
            if (engine.mouse().isDownLeft()) {
                if (config.scheduler.isTick(engine.loop().time())) {
                    Vector velocity = engine.mouse().position().subtract(attacker.position()).length(120);
                    Vector position = attacker.position();
                    GameFunctions.triggerShot(engine, memberConfig.teamId, velocity, position);
                }
            }
        }
    }
}

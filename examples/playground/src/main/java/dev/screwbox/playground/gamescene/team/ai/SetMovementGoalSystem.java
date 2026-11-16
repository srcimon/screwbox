package dev.screwbox.playground.gamescene.team.ai;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ai.PathMovementComponent;
import dev.screwbox.playground.gamescene.gamemodes.ctf.FlagPointComponent;
import dev.screwbox.playground.gamescene.team.TeamMemberComponent;

public class SetMovementGoalSystem implements EntitySystem {

    private static final Archetype BOTS = Archetype.of(PathMovementComponent.class, BotComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var bot : engine.environment().fetchAll(BOTS)) {
            var botConfig = bot.get(BotComponent.class);
            if (botConfig.scheduler.isTick(engine.loop().time())) {
                //TODO switch by mode
                int targetId = engine.environment().fetchAllHaving(FlagPointComponent.class).stream().filter(f -> !f.get(FlagPointComponent.class).team.equals(bot.get(TeamMemberComponent.class).team)).map(Entity::forceId).findFirst().get();
                engine.async().runExclusive(bot, () -> {
                    bot.get(PathMovementComponent.class).path = engine.navigation().findPath(bot.position(), engine.environment().fetchById(targetId).position()).orElse(null);
                });

            }

            //TODO PathMovementComponent should support entityId to auto chase entity
        }
    }
}

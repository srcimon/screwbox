package dev.screwbox.playground.gamescene.team.ai;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.ai.PathMovementComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.utils.ListUtil;
import dev.screwbox.playground.gamescene.gamemodes.ctf.FlagPointComponent;
import dev.screwbox.playground.gamescene.team.TeamMemberComponent;

import java.util.List;

public class SetMovementGoalSystem implements EntitySystem {

    private static final Archetype BOTS = Archetype.of(PathMovementComponent.class, BotComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var bot : engine.environment().fetchAll(BOTS)) {
            var botConfig = bot.get(BotComponent.class);
            if (botConfig.scheduler.isTick(engine.loop().time())) {
                List<Integer> targetIds = switch (botConfig.state) {
                    case FETCH_FLAG -> findEnemyFlagPoints(engine.environment(), bot);
                };
                if (!targetIds.isEmpty()) {
                    bot.get(PathMovementComponent.class).targetId = ListUtil.pickRandom(targetIds);
                }
            }
        }
    }

    private static List<Integer> findEnemyFlagPoints(Environment environment, Entity bot) {
        final Color team = bot.get(TeamMemberComponent.class).team;

        return environment.fetchAllHaving(FlagPointComponent.class).stream()
            .filter(flag -> !flag.get(FlagPointComponent.class).team.equals(team))
            .map(Entity::forceId)
            .toList();
    }
}

package dev.screwbox.playground.gamescene.teams.ai;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Time;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.ai.PathMovementComponent;
import dev.screwbox.core.environment.ai.TargetMovementComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.utils.ListUtil;
import dev.screwbox.core.utils.Scheduler;
import dev.screwbox.playground.gamescene.gamemodes.ctf.FlagPointComponent;
import dev.screwbox.playground.gamescene.teams.BotComponent;
import dev.screwbox.playground.gamescene.teams.TeamMemberComponent;
import dev.screwbox.playground.gamescene.teams.health.HealthComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class BotStateSystem implements EntitySystem {

    private static final Archetype BOTS = Archetype.of(PathMovementComponent.class, BotComponent.class);
    private static final Random RANDOM = new Random();

    //TODO introduce som kind of name
    record AiTarget(int targetId, int priority, Percent chance,
                    Duration reconsiderTime) implements Comparable<AiTarget> {

        @Override
        public int compareTo(AiTarget o) {
            return Integer.compare(priority, o.priority);
        }
    }

    @Override
    public void update(Engine engine) {
        for (final var bot : engine.environment().fetchAll(BOTS)) {
            var botConfig = bot.get(BotComponent.class);
            if (botConfig.scheduler.isTick(engine.loop().time()) || engine.environment().tryFetchById(bot.get(PathMovementComponent.class).targetId).isEmpty()) {
                final Color team = bot.get(TeamMemberComponent.class).team;

                List<AiTarget> possibleTargets = new ArrayList<>();
                possibleTargets.addAll(findEnemyFlagPoints(engine.environment(), team));
                possibleTargets.addAll(findNearbyNearlyDeadEnemies(engine.environment(), bot.position(), team));
                updateTarget(bot, possibleTargets);
            }
        }
    }

    //TODO add general interface and use Reflections.createInstances(AiBehaviour.class, "package")
    private Collection<AiTarget> findNearbyNearlyDeadEnemies(Environment environment, Vector position, Color team) {
        return environment.fetchAllHaving(TeamMemberComponent.class).stream()
            .filter(member -> member.get(TeamMemberComponent.class).team != team)
            .filter(member -> member.position().distanceTo(position) < 80)
            .filter(member -> member.get(HealthComponent.class).health.value() < 0.5)
            .map(entity -> new AiTarget(entity.forceId(), 80, Percent.half(), Duration.ofSeconds(5)))
            .toList();
    }

    private static List<AiTarget> findEnemyFlagPoints(Environment environment, Color team) {
        return environment.fetchAllHaving(FlagPointComponent.class).stream()
            .filter(flag -> !flag.get(FlagPointComponent.class).team.equals(team))
            .map(entity -> new AiTarget(entity.forceId(), 50, Percent.max(), Duration.oneSecond()))
            .toList();
    }

    private static void updateTarget(Entity bot, List<AiTarget> possibleTargets) {
        if (!possibleTargets.isEmpty()) {
            possibleTargets.sort(null);
            for (var target : possibleTargets.reversed()) {
                if (RANDOM.nextDouble() <= target.chance.value()) {
                    bot.get(PathMovementComponent.class).targetId = target.targetId;
                    bot.get(BotComponent.class).scheduler.skipUntil(target.reconsiderTime.addTo(Time.now()));
                    return;
                }
            }

        }
    }
}

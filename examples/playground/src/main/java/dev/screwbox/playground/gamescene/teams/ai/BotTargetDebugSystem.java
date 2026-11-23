package dev.screwbox.playground.gamescene.teams.ai;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.ai.PathMovementComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.LineDrawOptions;

import java.util.Objects;

import static java.util.Objects.nonNull;

public class BotTargetDebugSystem implements EntitySystem {

    private static final Archetype BOTS = Archetype.ofSpacial(PathMovementComponent.class);

    @Override
    public void update(Engine engine) {
        for(final var bot : engine.environment().fetchAll(BOTS)) {
            var targetId = bot.get(PathMovementComponent.class).targetId;
            if(nonNull(targetId)) {
               engine.environment().tryFetchById(targetId).ifPresent(target -> {
                   engine.graphics().world().drawLine(bot.position(), target.position(), LineDrawOptions.color(Color.WHITE.opacity(0.25)).strokeWidth(0.5).drawOrder(Order.DEBUG_OVERLAY.drawOrder()));
               });
            }
        }

    }
}

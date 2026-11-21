package dev.screwbox.playground.gamescene.teams.ai;

import dev.screwbox.core.Engine;
import dev.screwbox.core.assets.FontBundle;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.graphics.options.TextDrawOptions;
import dev.screwbox.playground.gamescene.teams.BotComponent;

@ExecutionOrder(Order.DEBUG_OVERLAY)
public class AiDebugSystem implements EntitySystem {

    private static final Archetype BOTS = Archetype.ofSpacial(BotComponent.class);

    @Override
    public void update(Engine engine) {
        TextDrawOptions options = TextDrawOptions.font(FontBundle.SKINNY_SANS).alignCenter().scale(0.5);
        for(final var bot : engine.environment().fetchAll(BOTS)) {
//            engine.graphics().world().drawText(bot.position().addY(10), bot.get(BotComponent.class).state.name(), options);
        }
    }
}

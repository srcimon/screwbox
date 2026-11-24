package dev.screwbox.playground.gamescene.teams.health;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.LineDrawOptions;

@ExecutionOrder(Order.DEBUG_OVERLAY)
public class HealthBarSystem implements EntitySystem {

    private static final Archetype LIVING = Archetype.of(HealthComponent.class);

    @Override
    public void update(Engine engine) {
        for(final var living : engine.environment().fetchAll(LIVING)) {
            Percent health = living.get(HealthComponent.class).health;
            var pos = living.bounds().position();
            var distance = health.rangeValue(0, 8);
            Vector start = pos.add(-4, 8);
//            engine.graphics().world().drawLine(start, start.addX(8), LineDrawOptions.color(Color.GREY).strokeWidth(2.0));
//            engine.graphics().world().drawLine(start, start.addX(distance), LineDrawOptions.color(Color.RED).strokeWidth(2.0));
        }
    }
}

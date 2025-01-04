package io.github.srcimon.screwbox.playgrounds.playercontrolls.world;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.logic.StateComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.drawoptions.RectangleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SystemTextDrawOptions;

@Order(Order.SystemOrder.PRESENTATION_WORLD)
public class WorldRenderSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        for (var entity : engine.environment().fetchAll(Archetype.ofSpacial())) {
            final var drawOptions = RectangleDrawOptions.filled(entity.hasComponent(PhysicsComponent.class)
                    ? Color.RED
                    : Color.WHITE);

            engine.graphics().world().drawRectangle(entity.bounds(), drawOptions);
            if(entity.hasComponent(StateComponent.class)) {
                engine.graphics().world().drawText(entity.position().add(10,-10), entity.get(StateComponent.class).state.getClass().getSimpleName(), SystemTextDrawOptions.systemFont("Arial").bold());
            }
        }
    }
}

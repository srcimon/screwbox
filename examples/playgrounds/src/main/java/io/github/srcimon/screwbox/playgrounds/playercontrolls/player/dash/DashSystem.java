package io.github.srcimon.screwbox.playgrounds.playercontrolls.player.dash;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.keyboard.Keyboard;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.PlayerControls;

@Order(Order.SystemOrder.SIMULATION_LATE)
public class DashSystem implements EntitySystem {

    private static final Archetype DASHERS = Archetype.of(DashComponent.class, PhysicsComponent.class);

    @Override
    public void update(Engine engine) {

        for (final var entity : engine.environment().fetchAll(DASHERS)) {
            var dashConfig = entity.get(DashComponent.class);
            if (engine.keyboard().isPressed(PlayerControls.DASH)) {
                if (dashConfig.dashStarted.isUnset() && !entity.hasComponent(DashingComponent.class)) {
                    dashConfig.dashStarted = engine.loop().time();
                    entity.add(new DashingComponent());
                    final var physics = entity.get(PhysicsComponent.class);
                    physics.momentum = Vector.of(
                            valueOfHighLow(PlayerControls.LEFT, PlayerControls.RIGHT, engine.keyboard()) * 400,
                            valueOfHighLow(PlayerControls.UP, PlayerControls.DOWN, engine.keyboard()) * 400

                    );
                }
            }
        }
    }

    //TODO refactor because its part of keyboard
    private double valueOfHighLow(final Enum<?> low, final Enum<?> high, Keyboard keyboard) {
        if (keyboard.isDown(low)) {
            return keyboard.isDown(high) ? 0 : -1;
        }
        return keyboard.isDown(high) ? 1 : 0;
    }
}

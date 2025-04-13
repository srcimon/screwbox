package io.github.srcimon.screwbox.platformer.systems;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.platformer.components.PlayerMarkerComponent;

@Order(Order.SystemOrder.SIMULATION_EARLY)
public class CameraShiftSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(PlayerMarkerComponent.class, RenderComponent.class);

    @Override
    public void update(Engine engine) {
        engine.environment().tryFetchSingleton(PLAYER).ifPresent(player -> {
            final double delta = engine.loop().delta(100);
            for (var target : engine.environment().fetchAllHaving(CameraTargetComponent.class)) {
                var configuration = target.get(CameraTargetComponent.class);
                configuration.shift = player.get(RenderComponent.class).options.isFlipHorizontal()
                        ? Vector.x(Math.max(-50, configuration.shift.x() - configuration.followSpeed * delta))
                        : Vector.x(Math.min(50, configuration.shift.x() + configuration.followSpeed * delta));
            }
        });
    }
}

package io.github.srcimon.screwbox.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.rendering.CameraTargetComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.platformer.components.PlayerMarkerComponent;

import java.util.Optional;

@Order(Order.SystemOrder.SIMULATION_BEGIN)
public class CameraShiftSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(PlayerMarkerComponent.class, RenderComponent.class);

    @Override
    public void update(Engine engine) {
        Optional<Entity> playerEntity = engine.environment().tryFetchSingleton(PLAYER);
        if (playerEntity.isEmpty()) {
            return;
        }

        double delta = engine.loop().delta();
        Entity player = playerEntity.get();
        var configuration = engine.environment().fetchSingletonComponent(CameraTargetComponent.class);
        if (player.get(RenderComponent.class).options.isFlipHorizontal()) {
            configuration.shift = Vector.of(
                    Math.max(-50,
                            configuration.shift.x() - configuration.followSpeed * delta * 100),
                    0);
        } else {
            configuration.shift = Vector.of(
                    Math.min(50,
                            configuration.shift.x() + configuration.followSpeed * delta * 100),
                    0);
        }
    }
}

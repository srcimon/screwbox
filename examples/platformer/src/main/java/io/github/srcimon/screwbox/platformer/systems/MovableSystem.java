package io.github.srcimon.screwbox.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.physics.Borders;
import io.github.srcimon.screwbox.platformer.components.MovableComponent;
import io.github.srcimon.screwbox.platformer.components.PlayerMarkerComponent;

import java.util.Optional;

import static io.github.srcimon.screwbox.core.utils.MathUtil.modifier;

@Order(Order.SystemOrder.SIMULATION_EARLY)
public class MovableSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.ofSpacial(PlayerMarkerComponent.class);
    private static final Archetype MOVABLES = Archetype.ofSpacial(MovableComponent.class, PhysicsComponent.class);

    @Override
    public void update(Engine engine) {
        Entity player = engine.environment().fetchSingleton(PLAYER);
        var playerMomentum = player.get(PhysicsComponent.class).momentum;
        var playerPosition = player.position();

        Optional<Entity> playerMovingBlock = engine.physics()
                .raycastFrom(playerPosition)
                .checkingFor(MOVABLES)
                .checkingBorders(Borders.VERTICAL_ONLY)
                .castingHorizontal(10 * modifier(playerMomentum.x()))
                .selectAnyEntity();

        if (playerMovingBlock.isPresent()) {
            Entity entity = playerMovingBlock.get();
            var physicsBody = entity.get(PhysicsComponent.class);
            var movable = entity.get(MovableComponent.class);
            physicsBody.momentum = Vector.of(movable.maxSpeed * modifier(playerMomentum.x()), physicsBody.momentum.y());
        }
    }
}

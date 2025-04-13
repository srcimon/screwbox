package dev.screwbox.platformer.systems;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.physics.Borders;
import dev.screwbox.platformer.components.MovableComponent;
import dev.screwbox.platformer.components.PlayerMarkerComponent;

import java.util.Optional;

import static dev.screwbox.core.utils.MathUtil.modifier;

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

package dev.screwbox.platformer.systems;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.HasOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.navigation.Borders;
import dev.screwbox.platformer.components.MovableComponent;
import dev.screwbox.platformer.components.PlayerMarkerComponent;

import static dev.screwbox.core.utils.MathUtil.modifier;

@HasOrder(Order.SIMULATION_EARLY)
public class MovableSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.ofSpacial(PlayerMarkerComponent.class);
    private static final Archetype MOVABLES = Archetype.ofSpacial(MovableComponent.class, PhysicsComponent.class);

    @Override
    public void update(Engine engine) {
        Entity player = engine.environment().fetchSingleton(PLAYER);
        var playerDirection = modifier(player.get(PhysicsComponent.class).velocity.x());

        engine.navigation()
                .raycastFrom(player.position())
                .checkingFor(MOVABLES)
                .checkingBorders(Borders.VERTICAL_ONLY)
                .castingHorizontal(10 * playerDirection)
                .selectAnyEntity().ifPresent(playerMovingBlock -> {
                    var physicsBody = playerMovingBlock.get(PhysicsComponent.class);
                    var movable = playerMovingBlock.get(MovableComponent.class);
                    physicsBody.velocity = Vector.of(movable.maxSpeed * playerDirection, physicsBody.velocity.y());
                });
    }
}

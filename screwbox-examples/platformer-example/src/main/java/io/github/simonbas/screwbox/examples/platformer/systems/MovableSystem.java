package io.github.simonbas.screwbox.examples.platformer.systems;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.Vector;
import io.github.simonbas.screwbox.core.entities.*;
import io.github.simonbas.screwbox.core.entities.components.PhysicsBodyComponent;
import io.github.simonbas.screwbox.core.entities.components.TransformComponent;
import io.github.simonbas.screwbox.core.physics.Borders;
import io.github.simonbas.screwbox.examples.platformer.components.MovableComponent;
import io.github.simonbas.screwbox.examples.platformer.components.PlayerMarkerComponent;

import java.util.Optional;

import static io.github.simonbas.screwbox.core.utils.MathUtil.modifier;

@Order(SystemOrder.SIMULATION_BEGIN)
public class MovableSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(PlayerMarkerComponent.class, TransformComponent.class);
    private static final Archetype MOVABLES = Archetype.of(MovableComponent.class, PhysicsBodyComponent.class,
            TransformComponent.class);

    @Override
    public void update(Engine engine) {
        Entity player = engine.entities().forcedFetch(PLAYER);
        var playerMomentum = player.get(PhysicsBodyComponent.class).momentum;
        var playerPosition = player.get(TransformComponent.class).bounds.position();

        Optional<Entity> playerMovingBlock = engine.physics()
                .raycastFrom(playerPosition)
                .checkingFor(MOVABLES)
                .checkingBorders(Borders.VERTICAL_ONLY)
                .castingHorizontal(10 * modifier(playerMomentum.x()))
                .selectAnyEntity();

        if (playerMovingBlock.isPresent()) {
            Entity entity = playerMovingBlock.get();
            var physicsBody = entity.get(PhysicsBodyComponent.class);
            var movable = entity.get(MovableComponent.class);
            physicsBody.momentum = Vector.of(movable.maxSpeed * modifier(playerMomentum.x()), physicsBody.momentum.y());
        }
    }
}

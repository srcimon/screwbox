package io.github.srcimon.screwbox.pathfinding.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.logic.StateComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.pathfinding.components.PlayerMovementComponent;
import io.github.srcimon.screwbox.pathfinding.states.BombTickingState;

public class PlayerControlSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.ofSpacial(
            PlayerMovementComponent.class, PhysicsComponent.class, RenderComponent.class);

    @Override
    public void update(final Engine engine) {
        final var player = engine.environment().fetchSingleton(PLAYER);
        player.get(PhysicsComponent.class).momentum = engine.keyboard().arrowKeysMovement(80);
        if (engine.keyboard().isPressed(Key.SPACE)) {

            var bomb = new Entity()
                    .add(new RenderComponent(player.get(RenderComponent.class).drawOrder - 1))
                    .add(new TransformComponent(player.bounds()))
                    .add(new StateComponent(new BombTickingState()));

            engine.environment().addEntity(bomb);
        }
    }

}

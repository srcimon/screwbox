package io.github.srcimon.screwbox.pathfinding.systems;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.logic.StateComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.keyboard.Key;
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
                    .bounds(player.bounds())
                    .add(new RenderComponent(player.get(RenderComponent.class).drawOrder - 1))
                    .add(new StateComponent(new BombTickingState()));

            engine.environment().addEntity(bomb);
        }
    }

}

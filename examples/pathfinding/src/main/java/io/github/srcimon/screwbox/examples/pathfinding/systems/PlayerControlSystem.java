package io.github.srcimon.screwbox.examples.pathfinding.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.environment.logic.StateComponent;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.core.keyboard.Keyboard;
import io.github.srcimon.screwbox.examples.pathfinding.components.PlayerMovementComponent;
import io.github.srcimon.screwbox.examples.pathfinding.states.BombTickingState;

public class PlayerControlSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(
            PlayerMovementComponent.class, PhysicsComponent.class, RenderComponent.class, TransformComponent.class);

    @Override
    public void update(final Engine engine) {
        final var player = engine.environment().forcedFetch(PLAYER);
        player.get(PhysicsComponent.class).momentum = determinMomemntum(engine.keyboard());

        if (engine.keyboard().isPressed(Key.SPACE)) {

            var bomb = new Entity()
                    .add(new RenderComponent(player.get(RenderComponent.class).drawOrder - 1))
                    .add(new TransformComponent(player.get(TransformComponent.class).bounds))
                    .add(new StateComponent(new BombTickingState()));

            engine.environment().addEntity(bomb);
        }
    }

    private Vector determinMomemntum(final Keyboard keyboard) {
        final var speed = 80;
        var x = 0.0;
        var y = 0.0;
        if (keyboard.isDown(Key.ARROW_LEFT)) {
            x = -speed;
        }
        if (keyboard.isDown(Key.ARROW_RIGHT)) {
            x = speed;
        }
        if (keyboard.isDown(Key.ARROW_UP)) {
            y = -speed;
        }
        if (keyboard.isDown(Key.ARROW_DOWN)) {
            y = speed;
        }
        return Vector.of(x, y);
    }

}

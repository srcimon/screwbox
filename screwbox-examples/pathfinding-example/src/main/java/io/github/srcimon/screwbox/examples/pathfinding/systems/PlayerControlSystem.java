package io.github.srcimon.screwbox.examples.pathfinding.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.entities.Archetype;
import io.github.srcimon.screwbox.core.entities.Entity;
import io.github.srcimon.screwbox.core.entities.EntitySystem;
import io.github.srcimon.screwbox.core.entities.components.PhysicsBodyComponent;
import io.github.srcimon.screwbox.core.entities.components.RenderComponent;
import io.github.srcimon.screwbox.core.entities.components.StateComponent;
import io.github.srcimon.screwbox.core.entities.components.TransformComponent;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.core.keyboard.Keyboard;
import io.github.srcimon.screwbox.examples.pathfinding.components.PlayerMovementComponent;
import io.github.srcimon.screwbox.examples.pathfinding.states.BombTickingState;

public class PlayerControlSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(
            PlayerMovementComponent.class, PhysicsBodyComponent.class, RenderComponent.class, TransformComponent.class);

    @Override
    public void update(final Engine engine) {
        final var player = engine.entities().forcedFetch(PLAYER);
        player.get(PhysicsBodyComponent.class).momentum = determinMomemntum(engine.keyboard());

        if (engine.keyboard().isPressed(Key.SPACE)) {

            var bomb = new Entity()
                    .add(new RenderComponent(player.get(RenderComponent.class).drawOrder - 1))
                    .add(new TransformComponent(player.get(TransformComponent.class).bounds))
                    .add(new StateComponent(new BombTickingState()));

            engine.entities().addEntity(bomb);
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

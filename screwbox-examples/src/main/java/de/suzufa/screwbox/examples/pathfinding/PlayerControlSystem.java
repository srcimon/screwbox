package de.suzufa.screwbox.examples.pathfinding;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.entityengine.components.StateComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.keyboard.Key;
import de.suzufa.screwbox.core.keyboard.Keyboard;

public class PlayerControlSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(
            PlayerMovementComponent.class, PhysicsBodyComponent.class, SpriteComponent.class, TransformComponent.class);

    @Override
    public void update(final Engine engine) {
        final var player = engine.entityEngine().forcedFetch(PLAYER);
        final Vector momentum = determinMomemntum(engine.keyboard());
        player.get(PhysicsBodyComponent.class).momentum = momentum;

        if (engine.keyboard().justPressed(Key.SPACE)) {

            var bomb = new Entity()
                    .add(new SpriteComponent(player.get(SpriteComponent.class).drawOrder))
                    .add(new TransformComponent(player.get(TransformComponent.class).bounds))
                    .add(new StateComponent(new BombTickingState()));

            engine.entityEngine().add(bomb);
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

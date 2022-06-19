package de.suzufa.screwbox.examples.pathfinding;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.keyboard.Key;
import de.suzufa.screwbox.core.keyboard.Keyboard;
import de.suzufa.screwbox.tiled.SpriteDictionary;
import de.suzufa.screwbox.tiled.TiledSupport;

public class PlayerMovementSystem implements EntitySystem {

    private static final SpriteDictionary PLAYER_SPRITES = TiledSupport.loadTileset("maze/player.json");

    @Override
    public void update(final Engine engine) {
        final var player = engine.entityEngine().forcedFetchById(1);
        final Vector momentum = determinMomemntum(engine.keyboard());
        player.get(PhysicsBodyComponent.class).momentum = momentum;

        player.get(SpriteComponent.class).sprite = momentum.isZero()
                ? PLAYER_SPRITES.findByName("standing")
                : PLAYER_SPRITES.findByName("walking");
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

package de.suzufa.screwbox.examples.pathfinding;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.UpdatePriority;
import de.suzufa.screwbox.core.entityengine.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.keyboard.Key;

public class PlayerMovementSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        var keyboard = engine.keyboard();
        var speed = 80;

        var player = engine.entityEngine().forcedFetchById(1);
        var body = player.get(PhysicsBodyComponent.class);

        engine.graphics().world().drawRectangle(player.get(TransformComponent.class).bounds, Color.RED);
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
        body.momentum = Vector.of(x, y);
    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PRESENTATION_UI_FOREGROUND;
    }

}

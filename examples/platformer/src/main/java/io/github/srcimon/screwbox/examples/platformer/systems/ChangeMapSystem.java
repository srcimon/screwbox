package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.SystemOrder;
import io.github.srcimon.screwbox.core.environment.logic.SignalComponent;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.examples.platformer.components.ChangeMapComponent;
import io.github.srcimon.screwbox.examples.platformer.scenes.GameScene;

import static io.github.srcimon.screwbox.core.scenes.SceneTransitionBundle.FADE_OVER_BLACK;

@Order(SystemOrder.SIMULATION_BEGIN)
public class ChangeMapSystem implements EntitySystem {

    private static final Archetype CHANGE_MAP_ZONES = Archetype.of(ChangeMapComponent.class, SignalComponent.class);

    @Override
    public void update(Engine engine) {
        if (engine.keyboard().isPressed(Key.SPACE)) {
            for (Entity entity : engine.environment().fetchAll(CHANGE_MAP_ZONES)) {
                if (entity.get(SignalComponent.class).isTriggered) {
                    engine.scenes()
                            .add(new GameScene(entity.get(ChangeMapComponent.class).fileName))
                            .switchTo(GameScene.class, FADE_OVER_BLACK);
                }

            }
        }
    }

}

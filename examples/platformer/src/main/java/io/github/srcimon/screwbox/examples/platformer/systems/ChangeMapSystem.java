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

@Order(SystemOrder.SIMULATION_BEGIN)
public class ChangeMapSystem implements EntitySystem {

    private static final Archetype CHANGE_MAP_ZONES = Archetype.of(ChangeMapComponent.class, SignalComponent.class);

    //TODO BUGFIX
    @Override
    public void update(Engine engine) {
        if (engine.keyboard().isPressed(Key.SPACE)) {
            System.out.println("SEARCH");
            for (Entity entity : engine.environment().fetchAll(CHANGE_MAP_ZONES)) {
                if (entity.get(SignalComponent.class).isTriggered) {
                    System.out.println("FOUND TRIGGERED");
                    engine.scenes().add(new GameScene(entity.get(ChangeMapComponent.class).fileName));
                    engine.scenes().switchTo(GameScene.class);
                }

            }
        }
    }

}

package dev.screwbox.platformer.systems;

import dev.screwbox.core.Ease;
import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.logic.TriggerAreaComponent;
import dev.screwbox.core.keyboard.Key;
import dev.screwbox.core.scenes.SceneTransition;
import dev.screwbox.core.scenes.animations.CirclesAnimation;
import dev.screwbox.platformer.components.ChangeMapComponent;
import dev.screwbox.platformer.scenes.GameScene;


@ExecutionOrder(Order.SIMULATION_EARLY)
public class ChangeMapSystem implements EntitySystem {

    private static final Archetype CHANGE_MAP_ZONES = Archetype.of(ChangeMapComponent.class, TriggerAreaComponent.class);

    @Override
    public void update(Engine engine) {
        if (engine.keyboard().isPressed(Key.SPACE)) {
            for (Entity entity : engine.environment().fetchAll(CHANGE_MAP_ZONES)) {
                if (entity.get(TriggerAreaComponent.class).isTriggered) {
                    engine.scenes()
                            .addOrReplace(new GameScene(entity.get(ChangeMapComponent.class).fileName))
                            .switchTo(GameScene.class, SceneTransition.custom()
                                    .outroDurationMillis(750)
                                    .outroEase(Ease.SINE_IN)
                                    .introAnimation(new CirclesAnimation())
                                    .introDurationMillis(1200));
                }
            }
        }
    }

}

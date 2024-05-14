package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.logic.SignalComponent;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.core.scenes.SceneTransition;
import io.github.srcimon.screwbox.core.scenes.animations.CirclesAnimation;
import io.github.srcimon.screwbox.examples.platformer.components.ChangeMapComponent;
import io.github.srcimon.screwbox.examples.platformer.scenes.GameScene;


@Order(Order.SystemOrder.SIMULATION_BEGIN)
public class ChangeMapSystem implements EntitySystem {

    private static final Archetype CHANGE_MAP_ZONES = Archetype.of(ChangeMapComponent.class, SignalComponent.class);

    @Override
    public void update(Engine engine) {
        if (engine.keyboard().isPressed(Key.SPACE)) {
            for (Entity entity : engine.environment().fetchAll(CHANGE_MAP_ZONES)) {
                if (entity.get(SignalComponent.class).isTriggered) {
                    engine.scenes()
                            .addOrReplace(new GameScene(entity.get(ChangeMapComponent.class).fileName))
                            .switchTo(GameScene.class, SceneTransition.custom()
                                    .extroDurationMillis(750)
                                    .extroEase(Ease.SINE_IN)
                                    .introAnimation(new CirclesAnimation())
                                    .introDurationMillis(1200));
                }
            }
        }
    }

}

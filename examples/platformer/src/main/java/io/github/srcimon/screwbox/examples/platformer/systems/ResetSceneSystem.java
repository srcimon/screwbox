package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.assets.SceneTransitionBundle;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.SystemOrder;
import io.github.srcimon.screwbox.examples.platformer.components.CurrentLevelComponent;
import io.github.srcimon.screwbox.examples.platformer.components.ResetSceneComponent;
import io.github.srcimon.screwbox.examples.platformer.scenes.DeadScene;

@Order(SystemOrder.SIMULATION_BEGIN)
public class ResetSceneSystem implements EntitySystem {

    private static final Archetype RESETTERS = Archetype.of(ResetSceneComponent.class);

    @Override
    public void update(Engine engine) {
        for (var resetter : engine.environment().fetchAll(RESETTERS)) {
            Time resetTime = resetter.get(ResetSceneComponent.class).atTime;
            if (Time.now().isAfter(resetTime)) {
                engine.audio().stopAllSounds();
                String currentLevel = engine.environment().fetchSingletonComponent(CurrentLevelComponent.class).name;
                engine.scenes()
                        .add(new DeadScene(currentLevel))
                        .switchTo(DeadScene.class, SceneTransitionBundle.FADEOUT);
            }
        }
    }
}

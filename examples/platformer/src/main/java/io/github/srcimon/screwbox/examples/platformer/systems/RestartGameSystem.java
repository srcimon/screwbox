package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.core.scenes.SceneTransition;
import io.github.srcimon.screwbox.core.scenes.animations.CirclesAnimation;
import io.github.srcimon.screwbox.examples.platformer.components.CurrentLevelComponent;
import io.github.srcimon.screwbox.examples.platformer.scenes.GameScene;

public class RestartGameSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        if (engine.keyboard().isDown(Key.SPACE)) {
            String currentLevel = engine.environment().fetchSingletonComponent(CurrentLevelComponent.class).name;
            engine.scenes().remove(GameScene.class);
            engine.scenes().add(new GameScene(currentLevel));
            engine.scenes().switchTo(GameScene.class, SceneTransition.custom()
                    .introAnimation(new CirclesAnimation())
                    .introDurationSeconds(1));
        }
    }

}

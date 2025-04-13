package io.github.srcimon.screwbox.platformer.systems;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.keyboard.Key;
import dev.screwbox.core.scenes.SceneTransition;
import dev.screwbox.core.scenes.animations.CirclesAnimation;
import io.github.srcimon.screwbox.platformer.components.CurrentLevelComponent;
import io.github.srcimon.screwbox.platformer.scenes.GameScene;

public class RestartGameSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        if (engine.keyboard().isDown(Key.SPACE) && !engine.scenes().isTransitioning()) {
            final String currentLevel = engine.environment().fetchSingletonComponent(CurrentLevelComponent.class).name;
            engine.scenes().remove(GameScene.class)
                    .add(new GameScene(currentLevel))
                    .switchTo(GameScene.class, SceneTransition.custom()
                            .introAnimation(new CirclesAnimation())
                            .introDurationSeconds(1));
        }
    }

}

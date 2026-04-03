package dev.screwbox.platformer.systems;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.keyboard.Key;
import dev.screwbox.core.scenes.SceneTransition;
import dev.screwbox.core.scenes.animations.SpriteFadeAnimation;
import dev.screwbox.platformer.scenes.PauseScene;

public class PauseSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        if (!engine.scenes().isTransitioning() &&
            (engine.keyboard().isPressed(Key.P)
             || engine.keyboard().isPressed(Key.ESCAPE)
             || !engine.window().hasFocus())) {

            engine.scenes().switchTo(PauseScene.class, SceneTransition.custom()
                .introAnimation(new SpriteFadeAnimation(engine.graphics().screen().takeScreenshot()))
                .introDurationMillis(500));
        }
    }

}

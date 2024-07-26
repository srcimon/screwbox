package io.github.srcimon.screwbox.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.core.scenes.SceneTransition;
import io.github.srcimon.screwbox.core.scenes.animations.SpriteFadeAnimation;
import io.github.srcimon.screwbox.platformer.scenes.PauseScene;

public class PauseSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        if (!engine.scenes().isTransitioning() && (engine.keyboard().isPressed(Key.P)
                || engine.keyboard().isPressed(Key.ESCAPE)
                || !engine.window().hasFocus())) {

            engine.audio().stopAllSounds();
            engine.scenes().switchTo(PauseScene.class, SceneTransition.custom()
                    .introAnimation(new SpriteFadeAnimation(engine.graphics().screen().takeScreenshot()))
                    .introDurationMillis(500));
        }
    }

}

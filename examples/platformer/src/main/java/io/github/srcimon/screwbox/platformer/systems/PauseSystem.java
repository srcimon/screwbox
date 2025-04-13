package io.github.srcimon.screwbox.platformer.systems;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.keyboard.Key;
import dev.screwbox.core.scenes.SceneTransition;
import dev.screwbox.core.scenes.animations.SpriteFadeAnimation;
import io.github.srcimon.screwbox.platformer.scenes.PauseScene;

import static dev.screwbox.core.graphics.options.SpriteDrawOptions.originalSize;

public class PauseSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        if (!engine.scenes().isTransitioning() && (engine.keyboard().isPressed(Key.P)
                || engine.keyboard().isPressed(Key.ESCAPE)
                || !engine.window().hasFocus())) {

            engine.audio().stopAllPlaybacks();

            SpriteFadeAnimation animation = new SpriteFadeAnimation(engine.graphics().screen().takeScreenshot(),
                    originalSize().rotation(engine.graphics().screen().rotation().invert()).ignoreOverlayShader());
            engine.scenes().switchTo(PauseScene.class, SceneTransition.custom()
                    .introAnimation(animation)
                    .introDurationMillis(500));
        }
    }

}

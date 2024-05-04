package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.scenes.SceneTransition;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.examples.platformer.scenes.PauseScene;

import static io.github.srcimon.screwbox.core.scenes.IntroAnimationBundle.FADE_OUT;

public class PauseSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        if (engine.keyboard().isPressed(Key.P)
                || engine.keyboard().isPressed(Key.ESCAPE)
                || !engine.window().hasFocus()) {

            engine.audio().stopAllSounds();
            engine.scenes().switchTo(PauseScene.class, SceneTransition.noExtroAnimation()
                    .introAnimation(FADE_OUT)
                    .introDurationMillis(500));
        }
    }

}

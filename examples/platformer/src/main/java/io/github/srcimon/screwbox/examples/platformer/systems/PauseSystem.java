package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.assets.SceneTransitionBundle;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.core.scenes.SceneTransition;
import io.github.srcimon.screwbox.examples.platformer.scenes.PauseScene;

public class PauseSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        if (engine.keyboard().isPressed(Key.P)
                || engine.keyboard().isPressed(Key.ESCAPE)
                || !engine.window().hasFocus()) {

            engine.audio().stopAllSounds();
            engine.scenes().switchTo(PauseScene.class, SceneTransitionBundle.FADEOUT);
        }
    }

}

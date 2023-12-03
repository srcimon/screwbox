package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.entities.Archetype;
import io.github.srcimon.screwbox.core.entities.Entity;
import io.github.srcimon.screwbox.core.entities.EntitySystem;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.examples.platformer.components.ScreenshotComponent;
import io.github.srcimon.screwbox.examples.platformer.scenes.PauseScene;

public class PauseSystem implements EntitySystem {

    private static final Archetype SCREENSHOT = Archetype.of(ScreenshotComponent.class);

    @Override
    public void update(Engine engine) {
        if (engine.keyboard().isPressed(Key.P)
                || engine.keyboard().isPressed(Key.ESCAPE)
                || !engine.window().hasFocus()) {

            Entity screenshotEntity = engine.entities().forcedFetch(SCREENSHOT);
            screenshotEntity.get(ScreenshotComponent.class).screenshot = engine.graphics().screen().takeScreenshot();
            engine.audio().stopAllSounds();
            engine.scenes().switchTo(PauseScene.class);
        }
    }

}

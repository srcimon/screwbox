package io.github.simonbas.screwbox.examples.platformer.systems;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.entities.Archetype;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.EntitySystem;
import io.github.simonbas.screwbox.core.keyboard.Key;
import io.github.simonbas.screwbox.examples.platformer.components.ScreenshotComponent;
import io.github.simonbas.screwbox.examples.platformer.scenes.PauseScene;

public class PauseSystem implements EntitySystem {

    private static final Archetype SCREENSHOT = Archetype.of(ScreenshotComponent.class);

    @Override
    public void update(Engine engine) {
        if (engine.keyboard().justPressed(Key.P)
                || engine.keyboard().justPressed(Key.ESCAPE)
                || !engine.window().hasFocus()) {

            Entity screenshotEntity = engine.entities().forcedFetch(SCREENSHOT);
            screenshotEntity.get(ScreenshotComponent.class).screenshot = engine.graphics().screen().takeScreenshot();
            engine.audio().stopAllSounds();
            engine.scenes().switchTo(PauseScene.class);
        }
    }

}

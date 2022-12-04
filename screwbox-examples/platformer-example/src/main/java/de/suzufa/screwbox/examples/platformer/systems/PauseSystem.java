package de.suzufa.screwbox.examples.platformer.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.keyboard.Key;
import de.suzufa.screwbox.examples.platformer.components.ScreenshotComponent;
import de.suzufa.screwbox.examples.platformer.scenes.PauseScene;

public class PauseSystem implements EntitySystem {

    private static final Archetype SCREENSHOT = Archetype.of(ScreenshotComponent.class);

    @Override
    public void update(Engine engine) {
        if (engine.keyboard().justPressed(Key.P)
                || engine.keyboard().justPressed(Key.ESCAPE)
                || !engine.graphics().window().hasFocus()) {

            Entity screenshotEntity = engine.entities().forcedFetch(SCREENSHOT);
            screenshotEntity.get(ScreenshotComponent.class).screenshot = engine.graphics().screen().takeScreenshot();
            engine.audio().stopAllSounds();
            engine.scenes().switchTo(PauseScene.class);
        }
    }

}

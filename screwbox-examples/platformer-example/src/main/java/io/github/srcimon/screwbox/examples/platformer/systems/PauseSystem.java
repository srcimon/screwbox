package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.entities.Archetype;
import io.github.srcimon.screwbox.core.entities.Entity;
import io.github.srcimon.screwbox.core.entities.EntitySystem;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.internal.BlurImageFilter;
import io.github.srcimon.screwbox.core.graphics.internal.ImageUtil;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.examples.platformer.components.ScreenshotComponent;
import io.github.srcimon.screwbox.examples.platformer.scenes.PauseScene;

public class PauseSystem implements EntitySystem {

    private static final Archetype SCREENSHOT = Archetype.of(ScreenshotComponent.class);

    @Override
    public void update(Engine engine) {
        if (engine.keyboard().justPressed(Key.P)
                || engine.keyboard().justPressed(Key.ESCAPE)
                || !engine.window().hasFocus()) {

            Entity screenshotEntity = engine.entities().forcedFetch(SCREENSHOT);
            var newImage = new BlurImageFilter(8).apply(ImageUtil.toBufferedImage(engine.graphics().screen().takeScreenshot().image(Time.now())));
            screenshotEntity.get(ScreenshotComponent.class).screenshot = Sprite.fromImage(newImage);
            engine.audio().stopAllSounds();
            engine.scenes().switchTo(PauseScene.class);
        }
    }

}

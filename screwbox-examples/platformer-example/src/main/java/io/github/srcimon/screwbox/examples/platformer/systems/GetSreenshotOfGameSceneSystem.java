package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ecosphere.Archetype;
import io.github.srcimon.screwbox.core.ecosphere.Entity;
import io.github.srcimon.screwbox.core.ecosphere.EntitySystem;
import io.github.srcimon.screwbox.examples.platformer.components.BackgroundHolderComponent;
import io.github.srcimon.screwbox.examples.platformer.components.ScreenshotComponent;
import io.github.srcimon.screwbox.examples.platformer.scenes.GameScene;

public class GetSreenshotOfGameSceneSystem implements EntitySystem {

    private static final Archetype SCREENSHOT = Archetype.of(ScreenshotComponent.class);
    private static final Archetype HOLDER = Archetype.of(BackgroundHolderComponent.class);

    @Override
    public void update(Engine engine) {
        var holder = engine.ecosphere().forcedFetch(HOLDER).get(BackgroundHolderComponent.class);
        Entity screenshotEntity = engine.scenes().entitiesOf(GameScene.class).forcedFetch(SCREENSHOT);
        holder.background = screenshotEntity.get(ScreenshotComponent.class).screenshot;

    }

}

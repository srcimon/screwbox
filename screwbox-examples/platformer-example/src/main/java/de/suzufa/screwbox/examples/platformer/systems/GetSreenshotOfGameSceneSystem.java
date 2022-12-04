package de.suzufa.screwbox.examples.platformer.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.examples.platformer.components.BackgroundHolderComponent;
import de.suzufa.screwbox.examples.platformer.components.ScreenshotComponent;
import de.suzufa.screwbox.examples.platformer.scenes.GameScene;

public class GetSreenshotOfGameSceneSystem implements EntitySystem {

    private static final Archetype SCREENSHOT = Archetype.of(ScreenshotComponent.class);
    private static final Archetype HOLDER = Archetype.of(BackgroundHolderComponent.class);

    @Override
    public void update(Engine engine) {
        var holder = engine.entities().forcedFetch(HOLDER).get(BackgroundHolderComponent.class);
        Entity screenshotEntity = engine.scenes().entitiesOf(GameScene.class).forcedFetch(SCREENSHOT);
        holder.background = screenshotEntity.get(ScreenshotComponent.class).screenshot;

    }

}

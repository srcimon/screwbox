package de.suzufa.screwbox.playground.debo.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.playground.debo.components.BackgroundHolderComponent;
import de.suzufa.screwbox.playground.debo.components.ScreenshotComponent;
import de.suzufa.screwbox.playground.debo.scenes.GameScene;

public class GetSreenshotOfGameSceneSystem implements EntitySystem {

    private static final Archetype SCREENSHOT = Archetype.of(ScreenshotComponent.class);
    private static final Archetype HOLDER = Archetype.of(BackgroundHolderComponent.class);

    @Override
    public void update(Engine engine) {
        var holder = engine.entityEngine().forcedFetchSingle(HOLDER).get(BackgroundHolderComponent.class);
        Entity screenshotEntity = engine.scenes().entityEngineOf(GameScene.class).forcedFetchSingle(SCREENSHOT);
        holder.background = screenshotEntity.get(ScreenshotComponent.class).screenshot;

    }

}

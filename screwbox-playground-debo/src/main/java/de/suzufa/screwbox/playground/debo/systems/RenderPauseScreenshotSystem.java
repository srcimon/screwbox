package de.suzufa.screwbox.playground.debo.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.playground.debo.components.BackgroundHolderComponent;

public class RenderPauseScreenshotSystem implements EntitySystem {

    private static final Archetype BACKGROUND = Archetype.of(BackgroundHolderComponent.class);

    @Override
    public void update(Engine engine) {
        var background = engine.entityEngine().forcedFetch(BACKGROUND);
        var backgroundSprite = background.get(BackgroundHolderComponent.class).background;
        engine.graphics().window().drawSprite(backgroundSprite, Offset.origin(), Percentage.half());
    }

}

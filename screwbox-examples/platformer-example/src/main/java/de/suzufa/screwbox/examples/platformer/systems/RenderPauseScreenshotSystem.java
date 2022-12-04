package de.suzufa.screwbox.examples.platformer.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Percent;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.examples.platformer.components.BackgroundHolderComponent;

public class RenderPauseScreenshotSystem implements EntitySystem {

    private static final Archetype BACKGROUND = Archetype.of(BackgroundHolderComponent.class);

    @Override
    public void update(Engine engine) {
        var background = engine.entities().forcedFetch(BACKGROUND);
        var backgroundSprite = background.get(BackgroundHolderComponent.class).background;
        engine.graphics().screen().drawSprite(backgroundSprite, Offset.origin(), Percent.half());
    }

}

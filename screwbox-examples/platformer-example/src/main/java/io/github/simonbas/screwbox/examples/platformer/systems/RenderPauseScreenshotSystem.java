package io.github.simonbas.screwbox.examples.platformer.systems;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.Percent;
import io.github.simonbas.screwbox.core.entities.Archetype;
import io.github.simonbas.screwbox.core.entities.EntitySystem;
import io.github.simonbas.screwbox.core.graphics.Offset;
import io.github.simonbas.screwbox.examples.platformer.components.BackgroundHolderComponent;

public class RenderPauseScreenshotSystem implements EntitySystem {

    private static final Archetype BACKGROUND = Archetype.of(BackgroundHolderComponent.class);

    @Override
    public void update(Engine engine) {
        var background = engine.entities().forcedFetch(BACKGROUND);
        var backgroundSprite = background.get(BackgroundHolderComponent.class).background;
        engine.graphics().screen().drawSprite(backgroundSprite, Offset.origin(), Percent.half());
    }

}

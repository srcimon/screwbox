package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.examples.platformer.components.BackgroundHolderComponent;

public class RenderPauseScreenshotSystem implements EntitySystem {

    private static final Archetype BACKGROUND = Archetype.of(BackgroundHolderComponent.class);

    @Override
    public void update(Engine engine) {
        var background = engine.environment().fetch(BACKGROUND);
        var backgroundSprite = background.get(BackgroundHolderComponent.class).background;
        engine.graphics().screen().drawSprite(backgroundSprite, Offset.origin(), Percent.half());
    }

}

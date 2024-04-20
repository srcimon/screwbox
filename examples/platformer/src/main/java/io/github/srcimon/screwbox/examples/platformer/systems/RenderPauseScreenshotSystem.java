package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.graphics.Offset;

import static io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions.originalSize;

public class RenderPauseScreenshotSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        var backgroundSprite = engine.scenes().lastSceneScreenshot().orElseThrow();
        engine.graphics().screen().drawSprite(backgroundSprite, Offset.origin(), originalSize().opacity(Percent.half()));
    }

}

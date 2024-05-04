package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.graphics.Offset;

import static io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions.originalSize;

public class RenderPauseScreenshotSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        engine.graphics().screen().lastScreenshot().ifPresent(screenshot ->
                engine.graphics().screen().drawSprite(screenshot, Offset.origin(), originalSize().opacity(Percent.half())));
        ;
    }

}

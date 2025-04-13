package io.github.srcimon.screwbox.platformer.systems;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.graphics.Offset;

import static dev.screwbox.core.graphics.options.SpriteDrawOptions.originalSize;

public class RenderPauseScreenshotSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        engine.graphics().screen().lastScreenshot().ifPresent(screenshot -> {
            var options = originalSize().rotation(engine.graphics().screen().rotation().invert()).opacity(0.5).ignoreOverlayShader();
            engine.graphics().canvas().drawSprite(screenshot, Offset.origin().substract(engine.graphics().canvas().offset()), options);
        });
    }

}

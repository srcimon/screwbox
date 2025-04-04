package io.github.srcimon.screwbox.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.graphics.Offset;

import static io.github.srcimon.screwbox.core.graphics.options.SpriteDrawOptions.originalSize;

public class RenderPauseScreenshotSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        engine.graphics().screen().lastScreenshot().ifPresent(screenshot -> {
            var options = originalSize().rotation(engine.graphics().screen().rotation().invert()).opacity(0.5).ignoreOverlayShader();
            engine.graphics().canvas().drawSprite(screenshot, Offset.origin().substract(engine.graphics().canvas().offset()), options);
        });
    }

}

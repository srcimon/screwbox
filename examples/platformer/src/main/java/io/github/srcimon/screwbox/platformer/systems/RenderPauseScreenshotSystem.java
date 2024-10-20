package io.github.srcimon.screwbox.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.internal.ImageUtil;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

import static io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions.originalSize;

public class RenderPauseScreenshotSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        try {
            ImageIO.write(ImageUtil.toBufferedImage(engine.graphics().screen().lastScreenshot().get().singleFrame().image()), "png", new File("demo.png"));
        } catch (IOException e) {
            throw new IllegalStateException("could not export png", e);
        }
        engine.graphics().screen().lastScreenshot().ifPresent(screenshot -> {
            final var croppedScreenshot = new Sprite(screenshot.singleFrame()
                    .extractArea(engine.graphics().canvas().offset(), engine.graphics().canvas().size()));

            engine.graphics().canvas().drawSprite(croppedScreenshot, Offset.origin(), originalSize().opacity(0.5)
                    .rotation(engine.graphics().screen().rotation().invert()));
        });
    }

}

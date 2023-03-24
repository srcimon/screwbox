package io.github.simonbas.screwbox.examples.presentation.scenes;

import io.github.simonbas.screwbox.core.Bounds;
import io.github.simonbas.screwbox.core.entities.Entities;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.components.RenderComponent;
import io.github.simonbas.screwbox.core.entities.components.TransformComponent;
import io.github.simonbas.screwbox.core.entities.systems.LogFpsSystem;
import io.github.simonbas.screwbox.core.entities.systems.RenderSystem;
import io.github.simonbas.screwbox.core.graphics.Sprite;
import io.github.simonbas.screwbox.core.graphics.internal.ImageUtil;
import io.github.simonbas.screwbox.core.mouse.MouseButton;
import io.github.simonbas.screwbox.core.scenes.Scene;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class PresentationScene implements Scene {

    private final List<Image> slides;

    public PresentationScene(List<Image> slides) {
        this.slides = slides;
    }

    @Override
    public void initialize(Entities entities) {
        entities.add(new RenderSystem())
                .add(new LogFpsSystem())
                .add(engine -> {
                    engine.graphics().updateCameraZoomBy(engine.mouse().unitsScrolled() / 10.0);
                    if (engine.mouse().isDown(MouseButton.LEFT)) {
                        engine.graphics().moveCameraBy(engine.mouse().drag());
                    }
                });
        var size = 16;
        var xOffset = 0;
        for (var slide : slides) {
            BufferedImage image = ImageUtil.toBufferedImage(slide);
            for (int x = 0; x <= slide.getWidth(null) - size; x += 16) {
                for (int y = 0; y <= slide.getHeight(null) - size; y += 16) {
                    Sprite sprite = Sprite.fromImage(image.getSubimage(x, y, size, size));
                    entities.add(new Entity()
                            .add(new TransformComponent(Bounds.atOrigin(xOffset, y, 16, 16)))
                            .add(new RenderComponent(sprite)));
                }
                xOffset += size;
            }
        }

    }
}

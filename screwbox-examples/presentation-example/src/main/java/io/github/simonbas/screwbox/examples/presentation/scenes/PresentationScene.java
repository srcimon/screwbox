package io.github.simonbas.screwbox.examples.presentation.scenes;

import io.github.simonbas.screwbox.core.Bounds;
import io.github.simonbas.screwbox.core.entities.Entities;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.components.RenderComponent;
import io.github.simonbas.screwbox.core.entities.components.TransformComponent;
import io.github.simonbas.screwbox.core.entities.systems.RenderSystem;
import io.github.simonbas.screwbox.core.graphics.Sprite;
import io.github.simonbas.screwbox.core.mouse.MouseButton;
import io.github.simonbas.screwbox.core.scenes.Scene;

import java.util.List;

public class PresentationScene implements Scene {

    private final List<Sprite> slides;

    public PresentationScene(List<Sprite> slides) {
        this.slides = slides;
    }

    @Override
    public void initialize(Entities entities) {
        entities.add(new RenderSystem())
                .add(engine -> {
                    engine.graphics().updateCameraZoomBy(engine.mouse().unitsScrolled() / 10.0);
                    if (engine.mouse().isDown(MouseButton.LEFT)) {
                        engine.graphics().moveCameraBy(engine.mouse().drag());
                    }
                });
        var currentX = 0;
        for (var slide : slides) {
            entities.add(new Entity()
                    .add(new TransformComponent(Bounds.atOrigin(currentX, 0, slide.size().width(), slide.size().height())))
                    .add(new RenderComponent(slide)));
            currentX += slide.size().width() + 10;
        }

    }
}

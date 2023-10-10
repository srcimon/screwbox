package io.github.srcimon.screwbox.examples.animation;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.entities.Entity;
import io.github.srcimon.screwbox.core.entities.EntitySystem;
import io.github.srcimon.screwbox.core.entities.components.*;
import io.github.srcimon.screwbox.core.entities.systems.LogFpsSystem;
import io.github.srcimon.screwbox.core.entities.systems.QuitOnKeyPressSystem;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.examples.animation.components.DotComponent;
import io.github.srcimon.screwbox.examples.animation.components.MeanderAroundPositionComponent;
import io.github.srcimon.screwbox.examples.animation.systems.MeanderAroundPositionSystem;
import io.github.srcimon.screwbox.examples.animation.systems.RenderDotsSystem;

import java.util.Random;

public class AnimationExample {

    public static void main(String[] args) {
        var screwBox = ScrewBox.createEngine("Animation Example");
        screwBox.entities()
                .add(new MeanderAroundPositionSystem())
                .add(new RenderDotsSystem())
                .add(new QuitOnKeyPressSystem(Key.ESCAPE))
                .add(new LogFpsSystem())
                .add(new EntitySystem() {
                    @Override
                    public void update(Engine engine) {
                        engine.graphics().updateCameraZoomBy(engine.mouse().unitsScrolled() / 10.0);
                    }
                });

        for (double x = -400; x < 400; x += 12) {
            for (double y = -400; y < 400; y += 12) {
                var pos = screwBox.graphics().worldPositionOf(Offset.at(x, y));
                screwBox.entities().add(new Entity()
                        .add(new TransformComponent(pos))
                        .add(new DotComponent())
                        .add(new MeanderAroundPositionComponent(pos, new Random().nextDouble(50, 500))));
            }
        }

        screwBox.start();
    }
}

package io.github.srcimon.screwbox.examples.animation;

import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.entities.Entity;
import io.github.srcimon.screwbox.core.entities.components.TransformComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.examples.animation.components.MeanderAroundPositionComponent;
import io.github.srcimon.screwbox.examples.animation.systems.MeanderAroundPositionSystem;
import io.github.srcimon.screwbox.examples.animation.systems.RenderAnimationSystem;

public class AnimationExample {

    public static void main(String[] args) {
        var screwBox = ScrewBox.createEngine("Animation Example");
        screwBox.entities().add(new MeanderAroundPositionSystem()).add(new RenderAnimationSystem());
        screwBox.entities().add(engine -> {
           if(engine.mouse().justClickedLeft()) {
               engine.entities().add(new Entity()
                       .add(new TransformComponent(engine.mouse().worldPosition()))
                       .add(new MeanderAroundPositionComponent(engine.mouse().worldPosition(), 50)));

           }
        });
        screwBox.start();
    }
}

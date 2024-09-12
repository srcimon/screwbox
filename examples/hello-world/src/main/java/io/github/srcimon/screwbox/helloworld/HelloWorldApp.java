package io.github.srcimon.screwbox.helloworld;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.particles.ParticleInteractionComponent;
import io.github.srcimon.screwbox.core.environment.physics.CursorAttachmentComponent;
import io.github.srcimon.screwbox.core.environment.physics.MovementTargetComponent;
import io.github.srcimon.screwbox.core.environment.physics.MovementTargetSystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;

public class HelloWorldApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Hello World");
        screwBox.graphics().light().setAmbientLight(Percent.max());

        screwBox.graphics().configuration().setLightmapScale(2);

        screwBox.environment()
                .enableAllFeatures()
                .addSystem(new MovementTargetSystem())
                .addSystem(engine -> {
                    if(engine.mouse().isPressedLeft()) {
                        engine.environment().fetchAllHaving(MovementTargetComponent.class).forEach(e -> e.get(MovementTargetComponent.class).position = engine.mouse().position());
                    }
                })
                .addEntity("follower",
                        new TransformComponent(),
                        new RenderComponent(SpriteBundle.BOX_STRIPED),
                        new PhysicsComponent(),
                        new MovementTargetComponent(Vector.zero()));

        screwBox.start();
    }
}
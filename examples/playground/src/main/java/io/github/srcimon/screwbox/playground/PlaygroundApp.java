package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.controls.JumpControlComponent;
import io.github.srcimon.screwbox.core.environment.controls.LeftRightControlComponent;
import io.github.srcimon.screwbox.core.environment.core.LogFpsSystem;
import io.github.srcimon.screwbox.core.environment.physics.GravityComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;
import io.github.srcimon.screwbox.core.graphics.options.PolygonDrawOptions;

import static io.github.srcimon.screwbox.core.Bounds.$$;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");

        screwBox.loop().unlockFps();

        screwBox.graphics().configuration().toggleFullscreen();
        screwBox.environment()
                .addEntity(new Entity().name("gravity")
                        .add(new GravityComponent(Vector.y(600))))

                .addEntity(new Entity().name("box")
                        .add(new RenderComponent(SpriteBundle.BOX_STRIPED))
                        .add(new FloatComponent())
                        .add(new FluidInteractionComponent())
                        .add(new PhysicsComponent())
                        .add(new JumpControlComponent())
                        .add(new LeftRightControlComponent())
                        .bounds($$(0, -100, 32, 32)))

                .addEntity(new Entity().name("water")
                        .bounds($$(-400, 0, 800, 300))
                        .add(new FluidComponent(30))
                        .add(new FluidRenderComponent(Color.BLUE.opacity(0.5))))

                .enableAllFeatures()
                .addSystem(new FluidRenderSystem())
                .addSystem(new LogFpsSystem())
                .addSystem(new FloatSystem())
                .addSystem(new FluidInteractionSystem())
                .addSystem(new MouseInteractionSystem())
                .addSystem(new UpdateWaterSystem());

        screwBox.start();
    }
}
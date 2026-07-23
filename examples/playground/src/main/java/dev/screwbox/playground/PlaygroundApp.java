package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.ai.BoidComponent;
import dev.screwbox.core.environment.ai.BoidObstacleComponent;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.physics.CursorAttachmentComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.environment.smoke.SmokeAffectorComponent;
import dev.screwbox.core.environment.smoke.SmokeEmitterComponent;
import dev.screwbox.core.environment.smoke.SmokeSystem;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.graphics.postfilter.CrtMonitorPostFilter;

import java.util.Random;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");

        screwBox.loop().unlockFps();
        screwBox.graphics().smoke().enable();
        screwBox.environment()
            .enableAllFeatures()
            .addSystem(new LogFpsSystem())
            .addSystem(new SmokeSystem());

        screwBox.environment().addSystem(x -> {
            x.graphics().smoke().affect(screwBox.mouse().position(), x.mouse().drag().invert().multiply(10*x.loop().delta()));
            x.graphics().smoke().emit(screwBox.mouse().position(), 80 * x.loop().delta(), Color.YELLOW);
        });
        screwBox.environment().addEntity(new Entity().bounds(screwBox.graphics().visibleArea()).add(new BoidObstacleComponent(), c -> c.isContainer =true));
        for(int i = 0; i < 4; i++) {
            Color random = Color.random();
            screwBox.environment().addEntity(new Entity().add(new PhysicsComponent())
                .bounds(Bounds.atPosition(new Random().nextDouble(-100, 100), new Random().nextDouble(-100, 100), 16, 16))
                .add(new RenderComponent(SpriteBundle.DOT_WHITE.get().replaceColor(Color.WHITE, random)))
                .add(new BoidComponent()).add(new SmokeEmitterComponent(10, random)));
        }

        screwBox.start();
    }
}
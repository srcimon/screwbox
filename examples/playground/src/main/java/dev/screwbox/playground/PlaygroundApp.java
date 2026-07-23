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

    static Color color = Color.WHITE;
    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");

        screwBox.loop().unlockFps();
        screwBox.graphics().smoke().enable();
        screwBox.environment()
            .enableAllFeatures()
            .addSystem(new LogFpsSystem())
            .addSystem(new SmokeSystem());

        screwBox.environment().addSystem(x -> {

            x.graphics().smoke().affect(screwBox.mouse().position(), x.mouse().position().subtract(x.graphics().visibleArea().position()).multiply(x.loop().delta()));
            x.graphics().smoke().emit(screwBox.mouse().position(), 880 * x.loop().delta(), color);
            if(x.mouse().isPressedLeft()) {
                color = Color.random();
            }
            x.graphics().camera().move(x.keyboard().wsadMovement(2));
        });
        screwBox.environment().addEntity(new Entity().bounds(screwBox.graphics().visibleArea()).add(new BoidObstacleComponent(), c -> c.isContainer =true));

        screwBox.start();
    }
}
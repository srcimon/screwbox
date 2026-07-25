package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.ai.BoidObstacleComponent;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.smoke.SmokeSystem;
import dev.screwbox.core.graphics.Color;

public class PlaygroundApp {

    static Color color = Color.WHITE;

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");

        screwBox.graphics().smoke().enable();
        screwBox.environment()
            .enableAllFeatures()
            .addSystem(new LogFpsSystem())
            .addSystem(new SmokeSystem());

        screwBox.environment().addSystem(x -> {

            x.graphics().smoke().affect(screwBox.mouse().position(), Vector.y(-50));
            x.graphics().smoke().emit(screwBox.mouse().position(), 500, color);
            if (x.mouse().isPressedLeft()) {
                color = Color.random();
            }
            x.graphics().camera().move(x.keyboard().wsadMovement(2));
        });
        screwBox.environment().addEntity(new Entity().bounds(screwBox.graphics().visibleArea()).add(new BoidObstacleComponent(), c -> c.isContainer = true));

        screwBox.start();
    }
}
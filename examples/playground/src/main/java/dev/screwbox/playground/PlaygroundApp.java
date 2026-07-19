package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.playground.render.GasRenderComponent;
import dev.screwbox.playground.render.GasRenderSystem;
import dev.screwbox.playground.simulate.GasSimulationComponent;
import dev.screwbox.playground.simulate.GasSimulationSystem;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");

        screwBox.loop().unlockFps();
        screwBox.environment()
            .enableAllFeatures()
            .addSystem(new LogFpsSystem())
            .addSystem(new GasSimulationSystem())
            .addSystem(new GasRenderSystem())
            .addEntity(new Entity().bounds(Bounds.atPosition(32, 32, 128, 64))
                .add(new GasRenderComponent())
                .add(new GasSimulationComponent(4)));

        screwBox.start();
    }
}
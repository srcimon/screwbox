package dev.screwbox.playground;

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
            .addSystem(engine -> {
                engine.graphics().camera().setZoomRestriction(0.1,10);
                engine.graphics().camera().move(engine.mouse().drag().multiply(0.1));
                engine.graphics().camera().changeZoomBy(engine.mouse().unitsScrolled()/10.0);
            })
            .addEntity(new Entity().bounds(screwBox.graphics().visibleArea().expand(-32))
                .add(new GasRenderComponent())
                .add(new GasSimulationComponent(4)));

        screwBox.start();
    }
}
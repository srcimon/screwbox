package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.smoke.internal.FluidSimulation;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");
        FluidSimulation fluidSimulation = new FluidSimulation(Size.of(80, 80));
        screwBox.loop().unlockFps();
        screwBox.environment()
            .enableAllFeatures()
            .addSystem(new LogFpsSystem())
                .addSystem(new EntitySystem() {
                    @Override
                    public void update(Engine engine) {

                        fluidSimulation.step(0.2, 0.1, 0.3, 4);
                    }
                });

        screwBox.start();
    }
}
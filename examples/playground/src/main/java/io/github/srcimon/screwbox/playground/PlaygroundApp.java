package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.environment.Entity;

import static io.github.srcimon.screwbox.core.Bounds.$$;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");
        screwBox.graphics().configuration().setUseAntialiasing(true);

        screwBox.environment()
                .addEntity(new Entity().name("water")
                        .bounds($$(-400, 0, 800, 80))
                        .add(new WaterComponent(20)))
                .addSystem(new DrawWaterSystem())
                .addSystem(new InteractWithWaterSystem())
                .addSystem(new UpdateWaterSystem());

        screwBox.start();
    }
}
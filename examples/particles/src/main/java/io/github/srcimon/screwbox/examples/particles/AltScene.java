package io.github.srcimon.screwbox.examples.particles;

import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.scenes.Scene;
import io.github.srcimon.screwbox.core.scenes.Scenes;

public class AltScene implements Scene {
    @Override
    public void populate(Environment environment) {
        environment.addSystem(engine -> engine.graphics().screen().fillWith(Color.RED));
    }
}

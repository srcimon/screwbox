package io.github.srcimon.screwbox.examples.helloworld;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.assets.ParticleOptionsBundle;
import io.github.srcimon.screwbox.core.assets.SpritesBundle;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.scenes.Scene;

public class BlueScene implements Scene {

    @Override
    public void populate(Environment environment) {
        environment
                .enableParticles()
                .enablePhysics()
                .enableTweening()
                .enableRendering()
                .addSystem(engine -> engine.particles().spawnMultiple(1, Vector.zero(), ParticleOptionsBundle.CONFETTI.get()
                        .sprites(SpritesBundle.DOT_BLUE_16)));
    }
}

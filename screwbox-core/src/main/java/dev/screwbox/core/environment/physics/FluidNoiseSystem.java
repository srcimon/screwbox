package dev.screwbox.core.environment.physics;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;

import java.util.Random;

public class FluidNoiseSystem implements EntitySystem {

    private static final Archetype NOISY_FLUIDS = Archetype.of(FluidComponent.class, FluidNoiseComponent.class);
    private static final Random RANDOM = new Random();


    @Override
    public void update(Engine engine) {
        for (final var entity : engine.environment().fetchAll(NOISY_FLUIDS)) {
            var strength = entity.get(FluidNoiseComponent.class).strength;
            var fluid = entity.get(FluidComponent.class);
            for (int i = 0; i < fluid.speed.length; i++) {
                fluid.speed[i] += RANDOM.nextDouble(-1, 1) * strength * engine.loop().delta();
            }
        }
    }
}

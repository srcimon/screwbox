package dev.screwbox.core.environment.physics;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;

import java.util.Random;

public class FluidNoiseSystem implements EntitySystem {

    private static final Archetype NOISY_FLUIDS = Archetype.of(FluidComponent.class, FluidNoiseComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var entity : engine.environment().fetchAll(NOISY_FLUIDS)) {
            var strength = entity.get(FluidNoiseComponent.class).strength;
            var fluid = entity.get(FluidComponent.class);
            fluid.speed[new Random().nextInt(0, fluid.speed.length - 1)] += strength * engine.loop().delta();
        }
    }
}

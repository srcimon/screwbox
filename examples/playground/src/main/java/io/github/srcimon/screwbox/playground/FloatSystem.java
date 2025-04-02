package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.FluidComponent;
import io.github.srcimon.screwbox.core.environment.physics.GravityComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;

import static io.github.srcimon.screwbox.core.utils.MathUtil.modifier;

public class FloatSystem implements EntitySystem {

    private static final Archetype FLUIDS = Archetype.ofSpacial(FluidComponent.class);
    private static final Archetype FLOATINGS = Archetype.ofSpacial(FloatComponent.class, PhysicsComponent.class);

    @Override
    public void update(Engine engine) {
        final var fluids = engine.environment().fetchAll(FLUIDS);
        final var floatings = engine.environment().fetchAll(FLOATINGS);
        final var gravity = engine.environment().tryFetchSingletonComponent(GravityComponent.class)
                .map(gravityComponent -> gravityComponent.gravity).orElse(Vector.zero());
        for (final var fluidEntity : fluids) {
            final FluidComponent fluid = fluidEntity.get(FluidComponent.class);
            for (final var floating : floatings) {
                final var floatOptions = floating.get(FloatComponent.class);
                final double height = getHeight(fluid, fluidEntity.bounds(), floating.bounds());
                if (height < 0) {
                    final var physics = floating.get(PhysicsComponent.class);
                    physics.momentum = physics.momentum.addY(engine.loop().delta(-floatOptions.buoyancy)).add(gravity.multiply(engine.loop().delta()).invert());
                    final double friction = floatOptions.friction * engine.loop().delta();
                    final double absX = Math.abs(physics.momentum.x());
                    final double absY = Math.abs(physics.momentum.y());
                    final double changeX = Math.clamp(modifier(physics.momentum.x()) * friction * -1, -absX, absX);
                    final double changeY = Math.clamp(modifier(physics.momentum.y()) * friction * -1, -absY, absY);
                    physics.momentum = physics.momentum.add(changeX, changeY);
                }
            }
        }
    }

    private double getHeight(final FluidComponent fluid, final Bounds fluidBounds, final Bounds bounds) {
        if (bounds.minX() < fluidBounds.minX() || bounds.maxX() > fluidBounds.maxX() || fluidBounds.maxY() < bounds.minY()) {
            return 0;
        }
        final double gap = fluidBounds.width() / (fluid.nodeCount - 1);
        final double xRelative = bounds.position().x() - fluidBounds.origin().x();
        final int nodeNr = (int) (xRelative / gap);

        final var height = (fluid.height[nodeNr] + fluid.height[nodeNr + 1]) / 2.0;
        return fluidBounds.minY() - bounds.position().y() + height;
    }
}

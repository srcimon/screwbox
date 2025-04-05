package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Line;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;

import static io.github.srcimon.screwbox.core.utils.MathUtil.modifier;

/**
 * Applies floating to physics {@link Entity entities} that also contain {@link FloatComponent}.
 *
 * @since 2.19.0
 */
public class FloatSystem implements EntitySystem {

    private static final Archetype FLUIDS = Archetype.ofSpacial(FluidComponent.class);
    private static final Archetype FLOATINGS = Archetype.ofSpacial(FloatComponent.class, PhysicsComponent.class);

    @Override
    public void update(final Engine engine) {
        final var fluids = engine.environment().fetchAll(FLUIDS);
        final var floatings = engine.environment().fetchAll(FLOATINGS);
        final var gravity = engine.environment().tryFetchSingletonComponent(GravityComponent.class)
                .map(gravityComponent -> gravityComponent.gravity).orElse(Vector.zero());
        for (final var fluidEntity : fluids) {
            final FluidComponent fluid = fluidEntity.get(FluidComponent.class);
            for (final var floating : floatings) {
                final var floatOptions = floating.get(FloatComponent.class);
                double height = 0;
                final Bounds fluidBounds = fluidEntity.bounds();
                final Bounds floatingBounds = floating.bounds();
                if (!(floatingBounds.minX() < fluidBounds.minX()) && !(floatingBounds.maxX() > fluidBounds.maxX()) && !(fluidBounds.maxY() < floatingBounds.minY())) {
                    final double gap = fluidBounds.width() / (fluid.nodeCount - 1);
                    final double xRelative = floatingBounds.position().x() - fluidBounds.origin().x();
                    final int nodeNr = (int) (xRelative / gap);
                    double heightLeft = fluid.height[nodeNr];
                    double heightRight = fluid.height[nodeNr + 1];
                    var rotationTarget = Rotation.of(Line.between(Vector.$(0,heightLeft), Vector.$(gap, heightRight)));
                    height = fluidBounds.minY() - floatingBounds.position().y() + (heightLeft + heightRight) / 2.0;
                    //TODO only on property = true
                    final var render = floating.get(RenderComponent.class);
                    var updatedRotation = render.options.rotation().degrees() + (rotationTarget.degrees() - render.options.rotation().degrees()) * engine.loop().delta()*10;
                    render.options = render.options.rotation(Rotation.degrees(updatedRotation));
                }

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

}

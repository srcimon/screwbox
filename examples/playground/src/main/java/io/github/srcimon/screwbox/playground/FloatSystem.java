package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Line;
import io.github.srcimon.screwbox.core.Path;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
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
        var gravity = engine.environment().tryFetchSingletonComponent(GravityComponent.class).map(g -> g.gravity).orElse(Vector.zero());

        for (final var fluid : fluids) {
            for (final var floating : floatings) {
                var physics = floating.get(PhysicsComponent.class);
                Fluid surface = fluid.get(FluidComponent.class).fluid;
                if (fluid.bounds().intersects(floating.bounds().expandTop(surface.maxHeight()))) {
                    var floatOptions = floating.get(FloatComponent.class);
                    var surfacePath = surface.surface(fluid.bounds());
                    double height = getHeight(surfacePath, floating.bounds().position());
                    if (height < 0) {
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

    private double getHeight(Path surfacePath, Vector position) {
        var normal = Line.normal(position, -1000);
        for (var segment : surfacePath.segments()) {
            var point = segment.intersectionPoint(normal);
            if (point != null) {
                return point.y() - position.y();
            }
        }
        return 0;
    }
}

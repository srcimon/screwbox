package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;

import java.util.Objects;

@Order(Order.SystemOrder.DEBUG_OVERLAY)
public class FloatRotationSystem implements EntitySystem {

    private static final Archetype FLOATINGS = Archetype.ofSpacial(FloatComponent.class, FloatRotationComponent.class, RenderComponent.class);

    @Override
    public void update(Engine engine) {
        for (var entity : engine.environment().fetchAll(FLOATINGS)) {
            final var wave = entity.get(FloatComponent.class).attachedWave;
            if (Objects.nonNull(wave)) {
                final var render = entity.get(RenderComponent.class);
                final double target = Rotation.of(wave).degrees() - 90;
                final double delta = target - render.options.rotation().degrees();
                final double change = Math.min(1, delta * engine.loop().delta() * 4);//TODO config
                render.options = render.options.rotation(render.options.rotation().addDegrees(change));
            }
        }

    }

}

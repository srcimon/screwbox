package io.github.srcimon.screwbox.core.ecosphere.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ecosphere.Archetype;
import io.github.srcimon.screwbox.core.ecosphere.Entity;
import io.github.srcimon.screwbox.core.ecosphere.EntitySystem;
import io.github.srcimon.screwbox.core.ecosphere.components.FadeOutComponent;
import io.github.srcimon.screwbox.core.ecosphere.components.RenderComponent;

public class FadeOutSystem implements EntitySystem {

    private static final Archetype FADEOUTS = Archetype.of(FadeOutComponent.class, RenderComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final Entity entity : engine.ecosphere().fetchAll(FADEOUTS)) {
            final var speed = entity.get(FadeOutComponent.class).speed;
            final var renderComponent = entity.get(RenderComponent.class);
            final double delta = engine.loop().delta();
            renderComponent.opacity = renderComponent.opacity.substract(speed * delta);
            if (renderComponent.opacity.isMinValue()) {
                engine.ecosphere().remove(entity);
            }
        }

    }

}

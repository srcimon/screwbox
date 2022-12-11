package de.suzufa.screwbox.core.entities.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.components.FadeOutComponent;
import de.suzufa.screwbox.core.entities.components.RenderComponent;

public class FadeOutSystem implements EntitySystem {

    private static final Archetype FADEOUTS = Archetype.of(FadeOutComponent.class, RenderComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final Entity entity : engine.entities().fetchAll(FADEOUTS)) {
            final var speed = entity.get(FadeOutComponent.class).speed;
            final var renderComponent = entity.get(RenderComponent.class);
            final double delta = engine.loop().delta();
            renderComponent.opacity = renderComponent.opacity.substract(speed * delta);
            if (renderComponent.opacity.isMinValue()) {
                engine.entities().remove(entity);
            }
        }

    }

}

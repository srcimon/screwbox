package de.suzufa.screwbox.core.entityengine.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.components.FadeOutComponent;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;

public class FadeOutSystem implements EntitySystem {

    private static final Archetype FADEOUTS = Archetype.of(FadeOutComponent.class, SpriteComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final Entity entity : engine.entityEngine().fetchAll(FADEOUTS)) {
            final var speed = entity.get(FadeOutComponent.class).speed;
            final var spriteComponent = entity.get(SpriteComponent.class);
            final double delta = engine.loop().delta();
            spriteComponent.opacity = spriteComponent.opacity.substract(speed * delta);
            if (spriteComponent.opacity.isMinValue()) {
                engine.entityEngine().remove(entity);
            }
        }

    }

}

package de.suzufa.screwbox.playground.debo.enemies.slime;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntityState;
import de.suzufa.screwbox.core.entities.components.SpriteComponent;
import de.suzufa.screwbox.playground.debo.components.DeathEventComponent;

public class SlimeAliveState implements EntityState {

    private static final long serialVersionUID = 1L;

    @Override
    public void enter(final Entity entity, Engine engine) {
        entity.get(SpriteComponent.class).sprite = SlimeResources.MOVING_SPRITE.newInstance();
    }

    @Override
    public EntityState update(final Entity entity, Engine engine) {
        if (entity.hasComponent(DeathEventComponent.class)) {
            return new SlimeDeadState();
        }
        return this;
    }

}

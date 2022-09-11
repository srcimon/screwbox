package de.suzufa.screwbox.playground.debo.enemies.tracer;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntityState;
import de.suzufa.screwbox.core.entities.components.SpriteComponent;
import de.suzufa.screwbox.playground.debo.components.DetectLineOfSightToPlayerComponent;

public class TracerInactiveState implements EntityState {

    private static final long serialVersionUID = 1L;

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.get(SpriteComponent.class).sprite = TracerResources.INACTIVE_SPRITE.newInstance();
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        return entity.get(DetectLineOfSightToPlayerComponent.class).isInLineOfSight
                ? new TracerActiveState()
                : this;
    }

}

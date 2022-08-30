package de.suzufa.screwbox.playground.debo.enemies.tracer;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntityState;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.playground.debo.components.DetectLineOfSightToPlayerComponent;
import de.suzufa.screwbox.playground.debo.components.FollowPlayerComponent;

public class TracerActiveState implements EntityState {

    private static final long serialVersionUID = 1L;

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.get(SpriteComponent.class).sprite = TracerResources.ACTIVE_SPRITE.newInstance();
        entity.add(new FollowPlayerComponent());
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        // TODO: fix
        engine.audio().playEffect(TracerResources.POEBELEI_SOUND);
        return entity.get(DetectLineOfSightToPlayerComponent.class).isInLineOfSight
                ? this
                : new TracerInactiveState();
    }

    @Override
    public void exit(Entity entity, Engine engine) {
        entity.remove(FollowPlayerComponent.class);
    }

}
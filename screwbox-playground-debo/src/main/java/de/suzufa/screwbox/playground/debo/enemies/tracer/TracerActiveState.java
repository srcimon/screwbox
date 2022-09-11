package de.suzufa.screwbox.playground.debo.enemies.tracer;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntityState;
import de.suzufa.screwbox.core.entities.components.SpriteComponent;
import de.suzufa.screwbox.playground.debo.components.DetectLineOfSightToPlayerComponent;
import de.suzufa.screwbox.playground.debo.components.FollowPlayerComponent;

public class TracerActiveState implements EntityState {

    private static final long serialVersionUID = 1L;

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.get(SpriteComponent.class).sprite = TracerResources.ACTIVE_SPRITE.newInstance();
        entity.add(new FollowPlayerComponent());
        engine.audio().playEffectLooped(TracerResources.POEBELEI_SOUND);
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        return entity.get(DetectLineOfSightToPlayerComponent.class).isInLineOfSight
                ? this
                : new TracerInactiveState();
    }

    @Override
    public void exit(Entity entity, Engine engine) {
        entity.remove(FollowPlayerComponent.class);
        engine.audio().stop(TracerResources.POEBELEI_SOUND);
    }

}
package de.suzufa.screwbox.playground.debo.enemies.tracer;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.assets.Asset;
import de.suzufa.screwbox.core.audio.Sound;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntityState;
import de.suzufa.screwbox.core.entities.components.SpriteComponent;
import de.suzufa.screwbox.playground.debo.components.DetectLineOfSightToPlayerComponent;
import de.suzufa.screwbox.playground.debo.components.FollowPlayerComponent;

public class TracerActiveState implements EntityState {

    private static final long serialVersionUID = 1L;

    public static final Asset<Sound> SOUND = Sound.assetFromFile("sounds/scream.wav");

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.get(SpriteComponent.class).sprite = TracerResources.ACTIVE_SPRITE.newInstance();
        entity.add(new FollowPlayerComponent());
        engine.audio().playEffectLooped(SOUND.get());
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
        engine.audio().stop(SOUND.get());
    }

}
package de.suzufa.screwbox.examples.pathfinding.states;

import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.audio.SoundPool;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntityState;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.examples.pathfinding.components.PlayerMovementComponent;
import de.suzufa.screwbox.tiled.TiledSupport;

public class BombExplosionState implements EntityState {

    private static final Sprite SPRITE = TiledSupport.loadTileset("maze/bomb.json").findByName("explosion");
    private static final SoundPool EXPLOSION = SoundPool.fromFile("maze/explosion.wav");
    private static final long serialVersionUID = 1L;

    private Time endOfAnimation;

    @Override
    public void enter(Entity entity, Engine engine) {
        Sprite sprite = SPRITE.newInstance();
        entity.get(SpriteComponent.class).sprite = sprite;
        endOfAnimation = engine.loop().metrics().timeOfLastUpdate().plus(sprite.duration());
        engine.audio().playEffect(EXPLOSION);
        Bounds bounds = entity.get(TransformComponent.class).bounds.inflated(8);
        List<Entity> entitiesInExplosionRange = engine.physics()
                .searchInRange(bounds)
                .ignoringEntitiesHaving(PlayerMovementComponent.class)
                .selectAll();

        engine.entityEngine().remove(entitiesInExplosionRange);
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if (endOfAnimation.isAfter(Time.now())) {
            engine.entityEngine().remove(entity);
        }
        return this;
    }

}

package de.suzufa.screwbox.examples.pathfinding;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.audio.SoundPool;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntityState;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.graphics.Sprite;
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
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if (engine.loop().metrics().timeOfLastUpdate().isAfter(endOfAnimation)) {
            engine.entityEngine().remove(entity);
        }
        return this;
    }

}

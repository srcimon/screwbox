package de.suzufa.screwbox.examples.pathfinding.states;

import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.audio.Sound;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntityState;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.examples.pathfinding.components.PlayerMovementComponent;
import de.suzufa.screwbox.tiled.SpriteDictionary;

public class BombExplosionState implements EntityState {

    private static final Sprite SPRITE = SpriteDictionary.fromJsonTileset("maze/bomb.json").findByName("explosion");
    private static final Sound EXPLOSION = Sound.fromFile("maze/explosion.wav");
    private static final long serialVersionUID = 1L;

    private Time endOfAnimation;

    @Override
    public void enter(Entity entity, Engine engine) {
        Sprite sprite = SPRITE.newInstance();
        entity.get(SpriteComponent.class).sprite = sprite;
        endOfAnimation = engine.loop().lastUpdate().plus(sprite.duration());
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
        if (Time.now().isAfter(endOfAnimation)) {
            engine.entityEngine().remove(entity);
        }
        return this;
    }

}

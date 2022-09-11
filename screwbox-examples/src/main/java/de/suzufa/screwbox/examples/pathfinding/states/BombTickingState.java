package de.suzufa.screwbox.examples.pathfinding.states;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntityState;
import de.suzufa.screwbox.core.entities.components.SpriteComponent;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.tiled.Tileset;

public class BombTickingState implements EntityState {

    private static final Sprite SPRITE = Tileset.fromJson("maze/bomb.json").findByName("ticking");
    private static final long serialVersionUID = 1L;

    private Time endOfAnimation;

    @Override
    public void enter(Entity entity, Engine engine) {
        Sprite sprite = SPRITE.newInstance();
        entity.get(SpriteComponent.class).sprite = sprite;
        endOfAnimation = engine.loop().lastUpdate().plus(sprite.duration());
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if (Time.now().isAfter(endOfAnimation)) {
            return new BombExplosionState();
        }
        return this;
    }

}

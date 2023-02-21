package io.github.simonbas.screwbox.examples.pathfinding.states;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.Time;
import io.github.simonbas.screwbox.core.assets.Asset;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.EntityState;
import io.github.simonbas.screwbox.core.entities.components.RenderComponent;
import io.github.simonbas.screwbox.core.graphics.Sprite;
import io.github.simonbas.screwbox.tiled.Tileset;

import java.io.Serial;

public class BombTickingState implements EntityState {

    private static final Asset<Sprite> SPRITE = Tileset.spriteAssetFromJson("bomb.json", "ticking");

    @Serial
    private static final long serialVersionUID = 1L;

    private Time endOfAnimation;

    @Override
    public void enter(Entity entity, Engine engine) {
        Sprite sprite = SPRITE.get().freshInstance();
        entity.get(RenderComponent.class).sprite = sprite;
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

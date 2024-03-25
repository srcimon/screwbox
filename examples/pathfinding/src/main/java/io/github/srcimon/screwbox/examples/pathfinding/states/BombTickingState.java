package io.github.srcimon.screwbox.examples.pathfinding.states;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.tiled.Tileset;

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
        endOfAnimation = sprite.duration().addTo(engine.loop().lastUpdate());
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if (Time.now().isAfter(endOfAnimation)) {
            return new BombExplosionState();
        }
        return this;
    }

}

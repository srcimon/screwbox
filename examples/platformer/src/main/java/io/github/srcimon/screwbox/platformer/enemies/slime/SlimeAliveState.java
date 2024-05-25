package io.github.srcimon.screwbox.platformer.enemies.slime;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.platformer.components.DeathEventComponent;

import static io.github.srcimon.screwbox.tiled.Tileset.spriteAssetFromJson;

public class SlimeAliveState implements EntityState {

    private static final long serialVersionUID = 1L;

    private static final Asset<Sprite> SPRITE = spriteAssetFromJson("tilesets/enemies/slime.json", "moving");

    @Override
    public void enter(final Entity entity, Engine engine) {
        entity.get(RenderComponent.class).sprite = SPRITE.get().freshInstance();
    }

    @Override
    public EntityState update(final Entity entity, Engine engine) {
        if (entity.hasComponent(DeathEventComponent.class)) {
            return new SlimeDeadState();
        }
        return this;
    }

}

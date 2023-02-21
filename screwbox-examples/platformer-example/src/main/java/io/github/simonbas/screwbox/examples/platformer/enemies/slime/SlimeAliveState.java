package io.github.simonbas.screwbox.examples.platformer.enemies.slime;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.assets.Asset;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.EntityState;
import io.github.simonbas.screwbox.core.entities.components.RenderComponent;
import io.github.simonbas.screwbox.core.graphics.Sprite;
import io.github.simonbas.screwbox.examples.platformer.components.DeathEventComponent;

import static io.github.simonbas.screwbox.tiled.Tileset.spriteAssetFromJson;

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

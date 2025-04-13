package dev.screwbox.platformer.enemies.slime;

import dev.screwbox.core.Engine;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.logic.EntityState;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.platformer.components.DeathEventComponent;

import static dev.screwbox.tiles.Tileset.spriteAssetFromJson;

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

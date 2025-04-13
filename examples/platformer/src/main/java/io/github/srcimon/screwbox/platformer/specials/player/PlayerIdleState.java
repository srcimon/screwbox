package io.github.srcimon.screwbox.platformer.specials.player;

import dev.screwbox.core.Engine;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.logic.EntityState;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.platformer.components.DeathEventComponent;
import io.github.srcimon.screwbox.platformer.components.PlayerControlComponent;

import static dev.screwbox.tiles.Tileset.spriteAssetFromJson;

public class PlayerIdleState implements EntityState {

    private static final long serialVersionUID = 1L;

    private static final Asset<Sprite> SPRITE = spriteAssetFromJson("tilesets/specials/player.json", "idle");

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.get(RenderComponent.class).sprite = SPRITE.get().freshInstance();
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if (entity.hasComponent(DeathEventComponent.class)) {
            return new PlayerDeathState();
        }

        var control = entity.get(PlayerControlComponent.class);
        if (control.digPressed || control.jumpPressed || control.leftPressed || control.rightPressed) {
            return new PlayerStandingState();
        }
        return this;
    }

}

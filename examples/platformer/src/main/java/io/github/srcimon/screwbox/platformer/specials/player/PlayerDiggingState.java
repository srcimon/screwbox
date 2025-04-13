package io.github.srcimon.screwbox.platformer.specials.player;

import dev.screwbox.core.Engine;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.logic.EntityState;
import dev.screwbox.core.environment.physics.CollisionDetailsComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.platformer.components.DeathEventComponent;
import io.github.srcimon.screwbox.platformer.components.DiggingComponent;
import io.github.srcimon.screwbox.platformer.components.PlayerControlComponent;

import java.io.Serial;

import static dev.screwbox.tiles.Tileset.spriteAssetFromJson;

public class PlayerDiggingState implements EntityState {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Asset<Sprite> SPRITE = spriteAssetFromJson("tilesets/specials/player.json", "digging");

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.get(RenderComponent.class).sprite = SPRITE.get();
        entity.add(new DiggingComponent());
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if (entity.hasComponent(DeathEventComponent.class)) {
            return new PlayerDeathState();
        }

        if (entity.get(CollisionDetailsComponent.class).touchesBottom) {
            return new PlayerStandingState();
        }
        if (entity.get(PlayerControlComponent.class).digPressed) {
            return this;
        }
        return new PlayerFallingState();
    }

    @Override
    public void exit(Entity entity, Engine engine) {
        entity.remove(DiggingComponent.class);
    }
}

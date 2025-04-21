package dev.screwbox.platformer.enemies.tracer;

import dev.screwbox.core.Engine;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.logic.EntityState;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.platformer.components.DetectLineOfSightToPlayerComponent;
import dev.screwbox.tiles.Tileset;

import java.io.Serial;

public class TracerInactiveState implements EntityState {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Asset<Sprite> SPRITE = Tileset.spriteAssetFromJson("tilesets/enemies/tracer.json", "inactive");

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.get(RenderComponent.class).sprite = SPRITE.get().freshInstance();
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        return entity.get(DetectLineOfSightToPlayerComponent.class).isInLineOfSight
                ? new TracerActiveState()
                : this;
    }

}

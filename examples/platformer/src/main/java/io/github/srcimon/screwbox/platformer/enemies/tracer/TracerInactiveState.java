package io.github.srcimon.screwbox.platformer.enemies.tracer;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.platformer.components.DetectLineOfSightToPlayerComponent;
import dev.screwbox.tiles.Tileset;

public class TracerInactiveState implements EntityState {

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

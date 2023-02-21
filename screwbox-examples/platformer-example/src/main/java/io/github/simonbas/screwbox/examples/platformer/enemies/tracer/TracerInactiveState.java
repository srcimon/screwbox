package io.github.simonbas.screwbox.examples.platformer.enemies.tracer;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.assets.Asset;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.EntityState;
import io.github.simonbas.screwbox.core.entities.components.RenderComponent;
import io.github.simonbas.screwbox.core.graphics.Sprite;
import io.github.simonbas.screwbox.examples.platformer.components.DetectLineOfSightToPlayerComponent;
import io.github.simonbas.screwbox.tiled.Tileset;

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

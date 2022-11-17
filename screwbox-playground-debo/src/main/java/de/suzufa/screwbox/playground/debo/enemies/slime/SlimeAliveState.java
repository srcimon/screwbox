package de.suzufa.screwbox.playground.debo.enemies.slime;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.assets.Asset;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntityState;
import de.suzufa.screwbox.core.entities.components.SpriteComponent;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.playground.debo.components.DeathEventComponent;
import de.suzufa.screwbox.tiled.Tileset;

public class SlimeAliveState implements EntityState {

    private static final long serialVersionUID = 1L;

    public static final Asset<Sprite> MOVING_SPRITE = Tileset.assetFromJson("tilesets/enemies/slime.json", "moving");

    @Override
    public void enter(final Entity entity, Engine engine) {
        entity.get(SpriteComponent.class).sprite = MOVING_SPRITE.get().newInstance();
    }

    @Override
    public EntityState update(final Entity entity, Engine engine) {
        if (entity.hasComponent(DeathEventComponent.class)) {
            return new SlimeDeadState();
        }
        return this;
    }

}

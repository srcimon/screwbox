package de.suzufa.screwbox.playground.debo.specials.player;

import static de.suzufa.screwbox.tiled.Tileset.assetFromJson;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.assets.Asset;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntityState;
import de.suzufa.screwbox.core.entities.components.SpriteComponent;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.playground.debo.components.DeathEventComponent;
import de.suzufa.screwbox.playground.debo.components.DiggingComponent;
import de.suzufa.screwbox.playground.debo.components.GroundDetectorComponent;
import de.suzufa.screwbox.playground.debo.components.PlayerControlComponent;

public class PlayerDiggingState implements EntityState {

    private static final long serialVersionUID = 1L;
    private static final Asset<Sprite> SPRITE = assetFromJson("tilesets/specials/player.json", "digging");

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.get(SpriteComponent.class).sprite = SPRITE.get();
        entity.add(new DiggingComponent());
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if (entity.hasComponent(DeathEventComponent.class)) {
            return new PlayerDeathState();
        }

        if (entity.get(GroundDetectorComponent.class).isOnGround) {
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

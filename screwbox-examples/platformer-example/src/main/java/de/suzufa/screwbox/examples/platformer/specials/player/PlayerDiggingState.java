package de.suzufa.screwbox.examples.platformer.specials.player;

import static de.suzufa.screwbox.tiled.Tileset.spriteAssetFromJson;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.assets.Asset;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntityState;
import de.suzufa.screwbox.core.entities.components.RenderComponent;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.examples.platformer.components.DeathEventComponent;
import de.suzufa.screwbox.examples.platformer.components.DiggingComponent;
import de.suzufa.screwbox.examples.platformer.components.GroundDetectorComponent;
import de.suzufa.screwbox.examples.platformer.components.PlayerControlComponent;

public class PlayerDiggingState implements EntityState {

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

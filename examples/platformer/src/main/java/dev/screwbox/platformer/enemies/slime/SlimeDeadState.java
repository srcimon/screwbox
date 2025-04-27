package dev.screwbox.platformer.enemies.slime;

import dev.screwbox.core.Engine;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.audio.Sound;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.logic.EntityState;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.environment.tweening.TweenComponent;
import dev.screwbox.core.environment.tweening.TweenDestroyComponent;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.platformer.components.CastShadowComponent;
import dev.screwbox.platformer.components.KillZoneComponent;
import dev.screwbox.platformer.components.KilledFromAboveComponent;

import java.io.Serial;

import static dev.screwbox.core.Duration.ofSeconds;
import static dev.screwbox.tiled.Tileset.spriteAssetFromJson;

public class SlimeDeadState implements EntityState {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Asset<Sound> KILL_SOUND = Sound.assetFromFile("sounds/kill.wav");
    private static final Asset<Sprite> SPRITE = spriteAssetFromJson("tilesets/enemies/slime.json", "dead");

    @Override
    public void enter(final Entity entity, Engine engine) {
        entity.get(RenderComponent.class).sprite = SPRITE.get().freshInstance();
        entity.remove(KillZoneComponent.class);
        entity.remove(CastShadowComponent.class);
        entity.remove(CastShadowComponent.class);
        entity.remove(KilledFromAboveComponent.class);
        entity.get(PhysicsComponent.class).ignoreCollisions = true;
        entity.add(new TweenComponent(ofSeconds(2)));
        entity.add(new TweenDestroyComponent());
        engine.audio().playSound(KILL_SOUND);
    }

    @Override
    public EntityState update(final Entity entity, Engine engine) {
        return this;
    }

}

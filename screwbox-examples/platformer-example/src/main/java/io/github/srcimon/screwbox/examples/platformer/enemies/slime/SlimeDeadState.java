package io.github.srcimon.screwbox.examples.platformer.enemies.slime;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntityState;
import io.github.srcimon.screwbox.core.environment.components.PhysicsBodyComponent;
import io.github.srcimon.screwbox.core.environment.components.RenderComponent;
import io.github.srcimon.screwbox.core.environment.components.TimeoutComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.examples.platformer.components.CastShadowComponent;
import io.github.srcimon.screwbox.examples.platformer.components.KillZoneComponent;
import io.github.srcimon.screwbox.examples.platformer.components.KilledFromAboveComponent;

import static io.github.srcimon.screwbox.tiled.Tileset.spriteAssetFromJson;

public class SlimeDeadState implements EntityState {

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
        entity.get(PhysicsBodyComponent.class).ignoreCollisions = true;
        entity.add(new TimeoutComponent(Time.now().plusSeconds(2)));
        engine.audio().playEffect(KILL_SOUND.get());
    }

    @Override
    public EntityState update(final Entity entity, Engine engine) {
        return this;
    }

}

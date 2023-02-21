package io.github.simonbas.screwbox.examples.platformer.enemies.slime;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.Time;
import io.github.simonbas.screwbox.core.assets.Asset;
import io.github.simonbas.screwbox.core.audio.Sound;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.EntityState;
import io.github.simonbas.screwbox.core.entities.components.PhysicsBodyComponent;
import io.github.simonbas.screwbox.core.entities.components.RenderComponent;
import io.github.simonbas.screwbox.core.entities.components.TimeoutComponent;
import io.github.simonbas.screwbox.core.graphics.Sprite;
import io.github.simonbas.screwbox.examples.platformer.components.CastShadowComponent;
import io.github.simonbas.screwbox.examples.platformer.components.KillZoneComponent;
import io.github.simonbas.screwbox.examples.platformer.components.KilledFromAboveComponent;

import static io.github.simonbas.screwbox.tiled.Tileset.spriteAssetFromJson;

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

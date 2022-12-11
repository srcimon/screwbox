package de.suzufa.screwbox.examples.platformer.enemies.slime;

import static de.suzufa.screwbox.tiled.Tileset.spriteAssetFromJson;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.assets.Asset;
import de.suzufa.screwbox.core.audio.Sound;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntityState;
import de.suzufa.screwbox.core.entities.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entities.components.RenderComponent;
import de.suzufa.screwbox.core.entities.components.TimeoutComponent;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.examples.platformer.components.CastShadowComponent;
import de.suzufa.screwbox.examples.platformer.components.KillZoneComponent;
import de.suzufa.screwbox.examples.platformer.components.KilledFromAboveComponent;

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

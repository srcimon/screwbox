package dev.screwbox.vacuum.player.movement;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.audio.SoundBundle;
import dev.screwbox.core.audio.SoundOptions;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.logic.EntityState;
import dev.screwbox.core.environment.particles.ParticleEmitterComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.particles.ParticleOptions;
import dev.screwbox.core.particles.SpawnMode;
import dev.screwbox.vacuum.player.attack.PlayerAttackControlComponent;

import static dev.screwbox.core.assets.Asset.asset;
import static dev.screwbox.tiled.Tileset.spriteAssetFromJson;

public class PlayerDashingState implements EntityState {

    private static final Asset<Sprite> DASHING = spriteAssetFromJson("tilesets/objects/player.json", "dashing");
    private static final Asset<ParticleOptions> SILHOUETTE = asset(() -> ParticleOptions.unknownSource()
            .sprite(spriteAssetFromJson("tilesets/objects/player.json", "silhouette"))
            .lifetimeMilliseconds(400)
            .animateOpacity(Percent.zero(), Percent.quarter()));

    @Override
    public void enter(Entity entity, Engine engine) {
        engine.audio().playSound(SoundBundle.JUMP, SoundOptions.playOnce().position(entity.position()));
        entity.add(new ParticleEmitterComponent(Duration.ofMillis(60), SpawnMode.POSITION, SILHOUETTE));
        entity.remove(PlayerAttackControlComponent.class);
        entity.remove(MovementControlComponent.class);
        entity.get(RenderComponent.class).sprite = DASHING.get().freshInstance();
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        return entity.hasComponent(DashComponent.class)
                ? this
                : new PlayerWalkingState();
    }

    @Override
    public void exit(Entity entity, Engine engine) {
        entity.remove(ParticleEmitterComponent.class);
        entity.add(new PlayerAttackControlComponent());
    }
}

package io.github.srcimon.screwbox.vacuum.player.movement;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.audio.SoundBundle;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.environment.particles.ParticleEmitterComponent;
import io.github.srcimon.screwbox.core.particles.ParticleOptions;
import io.github.srcimon.screwbox.vacuum.player.attack.PlayerAttackControl;

import static io.github.srcimon.screwbox.core.assets.Asset.asset;
import static io.github.srcimon.screwbox.tiled.Tileset.spriteAssetFromJson;

public class PlayerDashingState implements EntityState {

    private static final Asset<ParticleOptions> DASH_PARTICLE = asset(() -> ParticleOptions.unknownSource()
            .sprite(spriteAssetFromJson("tilesets/objects/player.json", "dashing"))
            .lifetimeMilliseconds(400)
            .animateOpacity(Percent.zero(), Percent.half()));

    @Override
    public void enter(Entity entity, Engine engine) {
        engine.audio().playSound(SoundBundle.JUMP ,entity.position());
        entity.add(new ParticleEmitterComponent(Duration.ofMillis(60), ParticleEmitterComponent.SpawnMode.POSITION, DASH_PARTICLE));
        entity.remove(PlayerAttackControl.class);
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
        entity.add(new PlayerAttackControl());
    }
}

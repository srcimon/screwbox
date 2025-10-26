package dev.screwbox.pathfinding.states;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Time;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.audio.Sound;
import dev.screwbox.core.audio.SoundOptions;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.logic.EntityState;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.particles.ParticlesBundle;
import dev.screwbox.pathfinding.components.PlayerMovementComponent;
import dev.screwbox.tiled.Tileset;

import java.io.Serial;
import java.util.List;

import static dev.screwbox.core.Duration.ofMillis;
import static dev.screwbox.core.graphics.options.CameraShakeOptions.lastingForDuration;
import static dev.screwbox.core.particles.ParticleOptions.particleSource;

public class BombExplosionState implements EntityState {

    private static final Asset<Sprite> SPRITE = Tileset.spriteAssetFromJson("bomb.json", "explosion");
    private static final Asset<Sound> EXPLOSION = Sound.assetFromFile("explosion.wav");

    @Serial
    private static final long serialVersionUID = 1L;

    private Time endOfAnimation;

    @Override
    public void enter(Entity entity, Engine engine) {
        engine.graphics().camera().shake(lastingForDuration(ofMillis(500)).strength(10));
        Sprite sprite = SPRITE.get().freshInstance();
        entity.get(RenderComponent.class).sprite = sprite;
        endOfAnimation = sprite.duration().addTo(engine.loop().time());
        engine.audio().playSound(EXPLOSION, SoundOptions.playOnce().position(entity.position()).randomness(0.1));
        engine.particles().spawnMultiple(8, entity.position(), ParticlesBundle.SMOKE_TRAIL);
        Bounds bounds = entity.bounds().expand(8);
        List<Entity> entitiesInExplosionRange = engine.navigation()
                .searchInArea(bounds)
                .ignoringEntitiesHaving(PlayerMovementComponent.class)
                .selectAll();

        for (var entityInExplosionRange : entitiesInExplosionRange) {
            engine.particles().spawnMultiple(4, entityInExplosionRange.position(), particleSource(entityInExplosionRange)
                    .randomBaseSpeed(8)
                    .sprite(entityInExplosionRange.get(RenderComponent.class).sprite)
                    .animateScale(0.0, 1)
                    .lifetimeSeconds(1));
        }

        engine.environment().remove(entitiesInExplosionRange);
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if (Time.now().isAfter(endOfAnimation)) {
            engine.environment().remove(entity);
        }
        return this;
    }

}

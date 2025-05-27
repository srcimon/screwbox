package dev.screwbox.core.particles;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Ease;
import dev.screwbox.core.Percent;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.assets.AssetBundle;
import dev.screwbox.core.graphics.SpriteBundle;

import static dev.screwbox.core.Duration.ofSeconds;

/**
 * An {@link AssetBundle} for {@link ParticleOptions}s shipped with the {@link ScrewBox} game engine.
 */
public enum ParticlesBundle implements AssetBundle<ParticleOptions> {

    SMOKE(ParticleOptions.unknownSource()
            .sprite(SpriteBundle.SMOKE)
            .baseSpeed(Vector.y(-100))
            .ease(Ease.SINE_IN_OUT)
            .randomStartScale(6, 8)
            .startOpacity(Percent.zero())
            .animateOpacity(Percent.zero(), Percent.of(0.1))
            .chaoticMovement(50, ofSeconds(1))
            .drawOrder(2)
            .randomStartRotation()
            .lifetimeSeconds(2)),

    CONFETTI(ParticleOptions.unknownSource()
            .sprites(SpriteBundle.DOT_BLUE, SpriteBundle.DOT_RED, SpriteBundle.DOT_YELLOW)
            .chaoticMovement(100, Duration.ofSeconds(1))
            .ease(Ease.SIN_IN_OUT_TWICE)
            .animateScale(2, 1)
            .startOpacity(Percent.zero())
            .randomLifeTimeSeconds(6, 7)
            .animateOpacity()),

    EXPLOSION(ParticleOptions.unknownSource()
            .sprites(SpriteBundle.DOT_YELLOW, SpriteBundle.DOT_RED)
            .animateOpacity()
            .animateScale(3, 1)
            .randomLifeTimeMilliseconds(800, 1500)
            .randomBaseSpeed(100, 400)),

    FALLING_LEAVES(ParticleOptions.unknownSource()
            .sprite(SpriteBundle.LEAVE_FALLING)
            .randomStartScale(1, 2)
            .animateHorizontalSpin()
            .randomStartRotation()
            .baseSpeed(Vector.$(60, 10))
            .chaoticMovement(50, ofSeconds(1))
            .animateOpacity()
            .randomLifeTimeSeconds(8, 12)
            .ease(Ease.SINE_IN_OUT)
            .castShadow()),

    SMOKE_TRAIL(ParticleOptions.unknownSource()
            .chaoticMovement(30, Duration.oneSecond())
            .sprite(SpriteBundle.DOT_WHITE)
            .randomBaseSpeed(10)
            .ease(Ease.SINE_IN_OUT)
            .randomLifeTimeMilliseconds(400, 800)
            .animateScale(0.01, 0.4)
            .relativeDrawOrder(1)),

    WATER_SPLASH(ParticleOptions.unknownSource()
            .chaoticMovement(30, Duration.oneSecond())
            .animateOpacity(Percent.of(0.1), Percent.of(0.2))
            .sprite(SpriteBundle.SPLASH)
            .randomRotation(-0.2, 0.2)
            .randomBaseSpeed(10)
            .ease(Ease.SINE_IN_OUT)
            .randomRotation(0.25)
            .randomLifeTimeMilliseconds(400, 800)
            .animateScale(0.4, 0.6)
            .relativeDrawOrder(1));

    private final ParticleOptions particleOptions;

    ParticlesBundle(final ParticleOptions particleOptions) {
        this.particleOptions = particleOptions;
    }

    @Override
    public Asset<ParticleOptions> asset() {
        return Asset.asset(() -> particleOptions);
    }
}

package io.github.srcimon.screwbox.core.assets;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.tweening.TweenMode;
import io.github.srcimon.screwbox.core.particles.ParticleOptions;

import static io.github.srcimon.screwbox.core.Duration.ofSeconds;

/**
 * An {@link AssetBundle} for {@link ParticleOptions}s shipped with the {@link ScrewBox} game engine.
 */
public enum ParticleOptionsBundle implements AssetBundle<ParticleOptions> {
    SMOKE(ParticleOptions.unknownSource()
            .sprite(SpriteBundle.SMOKE_16)
            .baseSpeed(Vector.y(-100))
            .tweenMode(TweenMode.SINE_IN_OUT)
            .randomStartScale(6, 8)
            .startOpacity(Percent.zero())
            .animateOpacity(Percent.zero(), Percent.of(0.1))
            .chaoticMovement(50, ofSeconds(1))
            .drawOrder(2)
            .randomStartRotation()
            .lifetimeSeconds(2)),
    CONFETTI(ParticleOptions.unknownSource()
            .sprites(SpriteBundle.DOT_BLUE_16, SpriteBundle.DOT_RED_16, SpriteBundle.DOT_YELLOW_16)
            .chaoticMovement(100, Duration.ofSeconds(1))
            .tweenMode(TweenMode.SIN_IN_OUT_TWICE)
            .animateScale(2, 1)
            .randomLifeTimeSeconds(6, 7)
            .animateOpacity()),
    EXPLOSION(ParticleOptions.unknownSource()
            .sprites(SpriteBundle.DOT_YELLOW_16, SpriteBundle.DOT_RED_16)
            .animateOpacity()
            .animateScale(3, 1)
            .randomLifeTimeMilliseconds(800, 1500)
            .randomBaseSpeed(100, 400));

    private final ParticleOptions particleDesigner;

    ParticleOptionsBundle(final ParticleOptions particleDesigner) {
        this.particleDesigner = particleDesigner;
    }

    @Override
    public Asset<ParticleOptions> asset() {
        return Asset.asset(() -> particleDesigner);
    }
}

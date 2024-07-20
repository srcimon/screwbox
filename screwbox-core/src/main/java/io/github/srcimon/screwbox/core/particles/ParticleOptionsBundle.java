package io.github.srcimon.screwbox.core.particles;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.assets.AssetBundle;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;

import static io.github.srcimon.screwbox.core.Duration.ofSeconds;

/**
 * An {@link AssetBundle} for {@link ParticleOptions}s shipped with the {@link ScrewBox} game engine.
 */
public enum ParticleOptionsBundle implements AssetBundle<ParticleOptions> {
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
            .randomStartRotation()
            .baseSpeed(Vector.$(60, 10))
            .chaoticMovement(50, ofSeconds(1))
            .animateOpacity()
            .randomLifeTimeSeconds(10, 15)
            .ease(Ease.SINE_IN_OUT)
            .castShadow());

    private final ParticleOptions particleOptions;

    ParticleOptionsBundle(final ParticleOptions particleOptions) {
        this.particleOptions = particleOptions;
    }

    @Override
    public Asset<ParticleOptions> asset() {
        return Asset.asset(() -> particleOptions);
    }
}

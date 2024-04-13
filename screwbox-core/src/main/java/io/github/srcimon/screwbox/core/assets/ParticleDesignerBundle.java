package io.github.srcimon.screwbox.core.assets;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.particles.ParticleOptions;
import io.github.srcimon.screwbox.core.environment.tweening.TweenMode;

import static io.github.srcimon.screwbox.core.Duration.ofSeconds;

//TODO JAVADOC AND TEST
public enum ParticleDesignerBundle implements AssetBundle<ParticleOptions> {
    SMOKE(new ParticleOptions()
            .sprite(SpritesBundle.SMOKE_16)
            .baseMovement(Vector.y(-100))
            .tweenMode(TweenMode.SINE_IN_OUT)
            .randomStartScale(6, 8)
            .animateOpacity(Percent.zero(), Percent.of(0.1))
            .baseMovement(Vector.$(0, -100))
            .chaoticMovement(50, ofSeconds(1))
            .drawOrder(2)
            .randomStartRotation()
            .lifetimeSeconds(2)),
    CONFETTI(new ParticleOptions()
            .sprites(SpritesBundle.DOT_BLUE_16, SpritesBundle.DOT_RED_16, SpritesBundle.DOT_YELLOW_16)
            .chaoticMovement(100, Duration.ofSeconds(1))
            .tweenMode(TweenMode.SIN_IN_OUT_TWICE)
            .animateScale(2, 1)
            .randomLifeTimeSeconds(6, 7)
            .animateOpacity());

    private final ParticleOptions particleDesigner;

    ParticleDesignerBundle(final ParticleOptions particleDesigner) {
        this.particleDesigner = particleDesigner;
    }

    @Override
    public Asset<ParticleOptions> asset() {
        return Asset.asset(() -> particleDesigner);
    }
}

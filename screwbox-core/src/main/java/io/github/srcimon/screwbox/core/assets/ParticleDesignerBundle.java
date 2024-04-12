package io.github.srcimon.screwbox.core.assets;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.particles.ParticleDesigner;
import io.github.srcimon.screwbox.core.environment.tweening.TweenMode;

import java.util.function.Supplier;

import static io.github.srcimon.screwbox.core.Duration.ofSeconds;

//TODO JAVADOC AND TEST
public enum ParticleDesignerBundle implements AssetBundle<ParticleDesigner> {

    SMOKE(() -> new ParticleDesigner()
            .sprite(SpritesBundle.SMOKE_16)
            .tweenMode(TweenMode.SINE_IN_OUT)
            .randomStartScale(6, 8)
            .animateOpacity(Percent.zero(), Percent.of(0.1))
            .baseMovement(Vector.$(0, -100))
            .chaoticMovement(50, ofSeconds(1))
            .drawOrder(2)
            .randomStartRotation()
            .lifetimeSeconds(2));

    private final Supplier<ParticleDesigner> particleDesigner;

    ParticleDesignerBundle(final Supplier<ParticleDesigner> particleDesigner) {
        this.particleDesigner = particleDesigner;
    }

    @Override
    public Asset<ParticleDesigner> asset() {
        return Asset.asset(particleDesigner); // wrap designer again so any acces returns a new designer to be customized
    }
}

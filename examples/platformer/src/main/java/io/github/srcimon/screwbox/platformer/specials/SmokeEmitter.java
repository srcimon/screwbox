package io.github.srcimon.screwbox.platformer.specials;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.particles.ParticleEmitterComponent;
import io.github.srcimon.screwbox.core.particles.ParticleOptions;
import io.github.srcimon.screwbox.tiled.GameObject;

import static io.github.srcimon.screwbox.core.Duration.ofSeconds;
import static io.github.srcimon.screwbox.core.Ease.SINE_IN_OUT;
import static io.github.srcimon.screwbox.core.graphics.SpriteBundle.SMOKE;

public class SmokeEmitter implements SourceImport.Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        return new Entity(object.id()).add(
                new TransformComponent(object.bounds()),
                new ParticleEmitterComponent(Duration.ofMillis(80), ParticleOptions.unknownSource()
                        .sprite(SMOKE)
                        .ease(SINE_IN_OUT)
                        .randomStartScale(1, 3)
                        .startOpacity(Percent.zero())
                        .animateOpacity(Percent.zero(), Percent.of(0.1))
                        .baseSpeed(Vector.y(-60))
                        .chaoticMovement(50, ofSeconds(1))
                        .drawOrder(object.layer().order())
                        .randomStartRotation()
                        .lifetimeSeconds(2)));
    }
}

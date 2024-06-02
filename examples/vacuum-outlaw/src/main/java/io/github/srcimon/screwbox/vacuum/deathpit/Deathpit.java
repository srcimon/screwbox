package io.github.srcimon.screwbox.vacuum.deathpit;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.particles.ParticleEmitterComponent;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;
import io.github.srcimon.screwbox.core.particles.ParticleOptions;
import io.github.srcimon.screwbox.tiled.GameObject;

import static io.github.srcimon.screwbox.core.Duration.ofSeconds;

public class Deathpit implements SourceImport.Converter<GameObject> {
    @Override
    public Entity convert(GameObject object) {
        return new Entity(object.id()).name("deathpit")
                .add(new TransformComponent(object.bounds()))
                .add(new ParticleEmitterComponent(Duration.ofMillis(200), ParticleOptions.unknownSource()
                        .sprite(SpriteBundle.ELECTRICITY_SPARCLE)
                        .drawOrder(object.layer().order())
                        .ease(Ease.SINE_IN_OUT)
                        .randomStartScale(1, 2)
                        .startOpacity(Percent.zero())
                        .animateOpacity(Percent.zero(), Percent.of(0.1))
                        .chaoticMovement(50, ofSeconds(1))
                        .randomStartRotation()
                        .lifetimeSeconds(2)));
    }
}

package dev.screwbox.platformer.specials;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.particles.ParticleEmitterComponent;
import dev.screwbox.core.particles.ParticleOptions;
import dev.screwbox.tiled.GameObject;

import static dev.screwbox.core.Duration.ofSeconds;
import static dev.screwbox.core.Ease.SINE_IN_OUT;
import static dev.screwbox.core.graphics.SpriteBundle.SMOKE;

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
                        .lifespanSeconds(2)));
    }
}

package dev.screwbox.platformer.zones;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Ease;
import dev.screwbox.core.Vector;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport.Converter;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.light.AerialLightComponent;
import dev.screwbox.core.environment.light.GlowComponent;
import dev.screwbox.core.environment.light.SpotLightComponent;
import dev.screwbox.core.environment.logic.TriggerAreaComponent;
import dev.screwbox.core.environment.particles.ParticleEmitterComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.ShaderBundle;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.particles.ParticleOptions;
import dev.screwbox.platformer.components.DeathEventComponent.DeathType;
import dev.screwbox.platformer.components.KillZoneComponent;
import dev.screwbox.platformer.components.PlayerMarkerComponent;
import dev.screwbox.tiled.GameObject;

public class KillZone implements Converter<GameObject> {

    private static final Asset<Sprite> SPARK = Asset.asset(() -> Sprite.pixel(Color.YELLOW).scaled(3).compileShader(ShaderBundle.UNDERWATER));
    private static final ParticleOptions LAVA_PARTICLES = ParticleOptions.unknownSource()
            .baseSpeed(Vector.y(-15))
            .randomLifeTimeMilliseconds(500, 2000)
            .animateOpacity()
            .animateOpacity()
            .sprite(SPARK)
            .drawOrder(10)
            .shaderSetup(ShaderBundle.UNDERWATER)
            .randomShaderOffset()
            .customize("light", particle -> particle.add(new SpotLightComponent(10, Color.BLACK.opacity(0.2))))
            .chaoticMovement(15, Duration.ofMillis(100))
            .ease(Ease.SINE_IN_OUT);

    @Override
    public Entity convert(GameObject object) {
        var deathType = object.properties().tryGetEnum("death-type", DeathType.class).orElse(DeathType.SPIKES);

        Entity entity = new Entity().add(
                new TriggerAreaComponent(Archetype.ofSpacial(PlayerMarkerComponent.class)),
                new KillZoneComponent(deathType),
                new TransformComponent(object.bounds()));

        if (deathType.equals(DeathType.LAVA)) {
            entity.add(new GlowComponent(30, Color.ORANGE.opacity(0.3)), glow -> glow.isRectangular = true);
            entity.add(new AerialLightComponent(Color.BLACK.opacity(0.7)));
            entity.add(new ParticleEmitterComponent(Duration.ofMillis((int) (10000 / entity.bounds().width())), LAVA_PARTICLES.source(entity)));
        }
        return entity;
    }

}

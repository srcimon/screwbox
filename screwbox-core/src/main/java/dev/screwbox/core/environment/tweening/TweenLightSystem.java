package dev.screwbox.core.environment.tweening;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.light.ConeLightComponent;
import dev.screwbox.core.environment.light.GlowComponent;
import dev.screwbox.core.environment.light.PointLightComponent;
import dev.screwbox.core.environment.light.SpotLightComponent;

import static java.util.Objects.nonNull;

/**
 * Updates the opacity of all light components of an {@link Entity}, that use tweening and have an {@link TweenLightComponent}.
 */
public class TweenLightSystem implements EntitySystem {

    private static final Archetype TWEENS = Archetype.of(TweenLightComponent.class, TweenComponent.class);

    @Override
    public void update(Engine engine) {
        for (final Entity tweenEntity : engine.environment().fetchAll(TWEENS)) {
            final var opacity = tweenEntity.get(TweenLightComponent.class);
            final var updatedOpacity = tweenEntity.get(TweenComponent.class).value.rangeValue(opacity.from.value(), opacity.to.value());
            var pointLight = tweenEntity.get(PointLightComponent.class);
            if (nonNull(pointLight)) {
                pointLight.color = pointLight.color.opacity(updatedOpacity);
            }

            var spotLight = tweenEntity.get(SpotLightComponent.class);
            if (nonNull(spotLight)) {
                spotLight.color = spotLight.color.opacity(updatedOpacity);
            }

            var coneLight = tweenEntity.get(ConeLightComponent.class);
            if (nonNull(coneLight)) {
                coneLight.color = coneLight.color.opacity(updatedOpacity);
            }

            var glow = tweenEntity.get(GlowComponent.class);
            if (nonNull(glow)) {
                glow.color = glow.color.opacity(updatedOpacity);
            }
        }
    }
}

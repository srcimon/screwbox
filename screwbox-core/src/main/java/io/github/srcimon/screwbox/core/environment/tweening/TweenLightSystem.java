package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.light.ConeLightComponent;
import io.github.srcimon.screwbox.core.environment.light.GlowComponent;
import io.github.srcimon.screwbox.core.environment.light.PointLightComponent;
import io.github.srcimon.screwbox.core.environment.light.SpotLightComponent;

import static java.util.Objects.nonNull;

public class TweenLightSystem implements EntitySystem {

    private static final Archetype TWEENS = Archetype.of(TweenLightOpacityComponent.class, TweenComponent.class);

    @Override
    public void update(Engine engine) {
        for (final Entity tweenEntity : engine.environment().fetchAll(TWEENS)) {
            final var opacity = tweenEntity.get(TweenLightOpacityComponent.class);
            final var advance = (opacity.to.value() - opacity.from.value()) * tweenEntity.get(TweenComponent.class).value.value();
            var pointLight = tweenEntity.get(PointLightComponent.class);
            if (nonNull(pointLight)) {
                pointLight.color = pointLight.color.opacity(advance);
            }

            var spotLight = tweenEntity.get(SpotLightComponent.class);
            if (nonNull(spotLight)) {
                spotLight.color = spotLight.color.opacity(advance);
            }

            var coneLight = tweenEntity.get(ConeLightComponent.class);
            if (nonNull(coneLight)) {
                coneLight.color = coneLight.color.opacity(advance);
            }

            var glow = tweenEntity.get(GlowComponent.class);
            if (nonNull(glow)) {
                glow.color = glow.color.opacity(advance);
            }
        }
    }
}

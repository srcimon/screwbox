package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.utils.ListUtil;

import static java.util.Objects.nonNull;

public class ParticleCustomizeRenderSystem implements EntitySystem {

    private static final Archetype PARTICLE_EMITTERS = Archetype.of(ParticleEmitterComponent.class, ParticleCustomizeRenderComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var particleEmitter : engine.environment().fetchAll(PARTICLE_EMITTERS)) {
            final var emitter = particleEmitter.get(ParticleEmitterComponent.class);
            final var renderConfig = particleEmitter.get(ParticleCustomizeRenderComponent.class);
            if (nonNull(emitter.particle)) {
                final Sprite sprite = ListUtil.randomFrom(renderConfig.sprites);
                emitter.particle.add(new RenderComponent(sprite, renderConfig.order, renderConfig.options));
            }
        }
    }
}

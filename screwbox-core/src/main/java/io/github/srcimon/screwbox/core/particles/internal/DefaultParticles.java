package io.github.srcimon.screwbox.core.particles.internal;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.assets.SpritesBundle;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.particles.ParticleComponent;
import io.github.srcimon.screwbox.core.environment.particles.ParticleOptions;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenDestroyComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenMode;
import io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;
import io.github.srcimon.screwbox.core.particles.Particles;

public class DefaultParticles implements Particles, Updatable {

    private static final Archetype PARTICLES = Archetype.of(ParticleComponent.class);
    private final Engine engine;

    private int particleCount = 0;

    public DefaultParticles(final Engine engine) {
        this.engine = engine;
    }

    @Override
    public void update() {
        particleCount = engine.environment().fetchAll(PARTICLES).size();
    }

    @Override
    public int particleCount() {
       return particleCount;
    }

    @Override
    public Particles spawn(final Vector position, final ParticleOptions options) {
        final var particle = createParticle(position, options);
        engine.environment().addEntity(particle);
        return this;
    }

    private Entity createParticle(final Vector position, final ParticleOptions options) {
        var physicsComponent = new PhysicsComponent();
        physicsComponent.ignoreCollisions = true;
        physicsComponent.gravityModifier = 0;
        physicsComponent.magnetModifier = 0;
        TransformComponent transfrom = new TransformComponent(position, 1, 1);
        RenderComponent render = new RenderComponent(SpritesBundle.DOT_BLUE_16, 0, SpriteDrawOptions.originalSize());
        final var entity = new Entity("particle");
        entity.add(new ParticleComponent());
        entity.add(new TweenComponent(Duration.ofSeconds(1), TweenMode.LINEAR_IN));
        entity.add(new TweenDestroyComponent());
        entity.add(physicsComponent);
        entity.add(transfrom);
        entity.add(render);
        for (final var entityCustomizer : options.customizers()) {
            entityCustomizer.accept(entity);
        }
        transfrom.bounds = Bounds.atPosition(position, render.sprite.size().width() * render.options.scale(), render.sprite.size().height() * render.options.scale());
        return entity;

    }
}

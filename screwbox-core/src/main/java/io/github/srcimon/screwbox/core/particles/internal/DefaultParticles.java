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

import java.util.Random;

public class DefaultParticles implements Particles, Updatable {

    private static final Random RANDOM = new Random();
    private static final Archetype PARTICLES = Archetype.of(ParticleComponent.class);

    private final Engine engine;

    private int particleCount = 0;
    private long particleSpawnCount = 0;

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
    public long particlesSpawnCount() {
        return particleSpawnCount;
    }

    @Override
    public Particles spawn(final Vector position, final ParticleOptions options) {
        final var particle = createParticle(position, options);
        particleSpawnCount++;
        engine.environment().addEntity(particle);
        return this;
    }

    @Override
    public Particles spawnMultiple(final int count, final Vector position, final ParticleOptions options) {
        for (int i = 0; i < count; i++) {
            spawn(position, options);
        }
        return this;
    }

    @Override
    public Particles spawn(Bounds bounds, ParticleOptions options) {
        final Vector spawnPosition = bounds.position().add(
                RANDOM.nextDouble(-0.5, 0.5) * bounds.width(),
                RANDOM.nextDouble(-0.5, 0.5) * bounds.height());
        return spawn(spawnPosition, options);
    }

    @Override
    public Particles spawnMultiple(final int count, final Bounds bounds, final ParticleOptions options) {
        for (int i = 0; i < count; i++) {
            spawn(bounds, options);
        }
        return this;
    }

    private Entity createParticle(final Vector position, final ParticleOptions options) {
        var physicsComponent = new PhysicsComponent();
        physicsComponent.ignoreCollisions = true;
        physicsComponent.gravityModifier = 0;
        physicsComponent.magnetModifier = 0;
        TransformComponent transfrom = new TransformComponent(position, 1, 1);
        RenderComponent render = new RenderComponent(SpritesBundle.DOT_BLUE_16, -1, SpriteDrawOptions.originalSize());
        final var entity = new Entity("particle");//TODO add source name or id
        entity.add(new ParticleComponent());
        entity.add(new TweenComponent(Duration.ofSeconds(1), TweenMode.LINEAR_OUT));
        entity.add(new TweenDestroyComponent());
        entity.add(physicsComponent);
        entity.add(transfrom);
        entity.add(render);
        for (final var entityCustomizer : options.customizers()) {
            entityCustomizer.accept(entity);
        }
        if (render.drawOrder == -1) {
            if (options.source() != null) {
                var originalRender = options.source().get(RenderComponent.class);
                if(originalRender != null) {
                    render.drawOrder = originalRender.drawOrder;
                }
            } else {
                render.drawOrder = 0;
            }
        }
        transfrom.bounds = Bounds.atPosition(position, render.sprite.size().width() * render.options.scale(), render.sprite.size().height() * render.options.scale());
        return entity;

    }
}

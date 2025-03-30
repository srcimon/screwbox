package io.github.srcimon.screwbox.core.particles.internal;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.particles.ParticleComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenDestroyComponent;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;
import io.github.srcimon.screwbox.core.graphics.options.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.graphics.internal.AttentionFocus;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;
import io.github.srcimon.screwbox.core.particles.ParticleOptions;
import io.github.srcimon.screwbox.core.particles.Particles;
import io.github.srcimon.screwbox.core.scenes.internal.DefaultScenes;

import java.util.Random;

public class DefaultParticles implements Particles, Updatable {

    private static final Random RANDOM = new Random();
    private static final Archetype PARTICLES = Archetype.of(ParticleComponent.class);

    private final DefaultScenes scenes;
    private final AttentionFocus attentionFocus;

    private boolean particleCountRefreshed = false;
    private long particleCount = 0;
    private int particleLimit = 10000;
    private double spawnDistance = 1000;
    private long particleSpawnCount = 0;

    public DefaultParticles(final DefaultScenes scenes, final AttentionFocus attentionFocus) {
        this.scenes = scenes;
        this.attentionFocus = attentionFocus;
    }

    @Override
    public boolean isWithinSpawnArea(final Vector position) {
        return attentionFocus.isWithinDistanceToVisibleArea(position, spawnDistance);
    }

    @Override
    public long particleCount() {
        if (particleCountRefreshed) {
            return particleCount;
        }
        particleCountRefreshed = true;
        particleCount = scenes.activeEnvironment().entityCount(PARTICLES);
        return particleCount;
    }

    @Override
    public long particlesSpawnCount() {
        return particleSpawnCount;
    }

    @Override
    public int particleLimit() {
        return particleLimit;
    }

    @Override
    public Particles setSpawnDistance(final double spawnDistance) {
        this.spawnDistance = spawnDistance;
        return this;
    }

    @Override
    public double spawnDistance() {
        return spawnDistance;
    }

    @Override
    public Particles setParticleLimit(final int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("particle limit must be positive");
        }
        particleLimit = limit;
        return this;
    }

    @Override
    public Particles spawn(final Vector position, final ParticleOptions options) {
        if (particleLimit > particleCount() && isWithinSpawnArea(position)) {
            final var particle = createParticle(position, options);
            particleSpawnCount++;
            particleCount++;
            scenes.activeEnvironment().addEntity(particle);
        }
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
    public Particles spawn(final Bounds bounds, final ParticleOptions options) {
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

    @Override
    public void update() {
        particleCountRefreshed = false;
    }

    private Entity createParticle(final Vector position, final ParticleOptions options) {
        final var render = new RenderComponent(SpriteBundle.DOT_BLUE, -1, SpriteDrawOptions.originalSize());
        final var entity = new Entity()
                .name("particle-" + (particleSpawnCount + 1))
                .add(new ParticleComponent())
                .add(new TweenComponent(Duration.ofSeconds(1), Ease.LINEAR_OUT))
                .add(new TweenDestroyComponent())
                .add(new TransformComponent(position,
                        render.sprite.width() * render.options.scale(),
                        render.sprite.height() * render.options.scale()))
                .add(render)
                .add(new PhysicsComponent(), physics -> {
                    physics.ignoreCollisions = true;
                    physics.gravityModifier = 0;
                    physics.magnetModifier = 0;
                });
        for (final var entityCustomizer : options.modifiers()) {
            entityCustomizer.accept(entity);
        }
        if (render.drawOrder == -1) {
            if (options.source() != null) {
                var originalRender = options.source().get(RenderComponent.class);
                if (originalRender != null) {
                    render.drawOrder = originalRender.drawOrder;
                }
            } else {
                render.drawOrder = 0;
            }
        }
        return entity;
    }
}

package dev.screwbox.core.particles.internal;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Duration;
import dev.screwbox.core.Ease;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.particles.ParticleComponent;
import dev.screwbox.core.environment.physics.TailwindPropelledComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.environment.tweening.TweenComponent;
import dev.screwbox.core.environment.tweening.TweenDestroyComponent;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.graphics.internal.AttentionFocus;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.loop.internal.Updatable;
import dev.screwbox.core.particles.ParticleOptions;
import dev.screwbox.core.particles.Particles;
import dev.screwbox.core.particles.SpawnMode;
import dev.screwbox.core.scenes.internal.DefaultScenes;
import dev.screwbox.core.utils.Validate;

import java.util.Random;

import static java.util.Objects.nonNull;

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
        Validate.zeroOrPositive(limit, "particle limit must be positive");
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
    public Particles spawn(Bounds bounds, SpawnMode spawnMode, ParticleOptions options) {
        var area = spawnMode.spawnArea(bounds);
        return spawn(area, options);
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
                .add(render)
                .add(new TailwindPropelledComponent())
                .add(new PhysicsComponent(), physics -> {
                    physics.ignoreCollisions = true;
                    physics.gravityModifier = 0;
                    physics.magnetModifier = 0;
                });
        for (final var entityCustomizer : options.modifiers()) {
            entityCustomizer.accept(entity);
        }
        if (render.drawOrder == -1) {
            if (nonNull(options.source())) {
                var originalRender = options.source().get(RenderComponent.class);
                if (nonNull(originalRender)) {
                    render.drawOrder = originalRender.drawOrder + options.relativeDrawOrder();
                }
            } else {
                render.drawOrder = options.relativeDrawOrder();
            }
        } else {
            render.drawOrder += options.relativeDrawOrder();
        }
        return entity.bounds(Bounds.atPosition(position,
                render.sprite.width() * render.options.scale(),
                render.sprite.height() * render.options.scale()));
    }
}

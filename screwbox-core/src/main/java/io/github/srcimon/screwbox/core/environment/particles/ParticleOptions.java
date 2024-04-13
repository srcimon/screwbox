package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.environment.physics.ChaoticMovementComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenMode;
import io.github.srcimon.screwbox.core.environment.tweening.TweenOpacityComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenScaleComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.utils.ListUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.util.Objects.nonNull;

/**
 * Is used by the {@link ParticleEmitterComponent} to create {@link Environment#entities()} that are used for particle effects.
 *
 * @see Environment#enableParticles()
 */
public class ParticleOptions implements Serializable {
    //TODO name, origin?
    private static final Random RANDOM = new Random();
    private static final String PREFIX = "default-";

    /**
     * Used to add special customization to the particles that is not already available via {@link ParticleOptions}
     * methods.
     *
     * @see #customize(String, ParticleCustomizer)
     */
    @FunctionalInterface
    public interface ParticleCustomizer extends Consumer<Entity>, Serializable {

    }

    private final Map<String, ParticleCustomizer> customizers;

    /**
     * Creates a new instance.
     */
    public ParticleOptions() {
        this(new HashMap<>());
    }

    private ParticleOptions(final Map<String, ParticleCustomizer> customizers) {
        this.customizers = Collections.unmodifiableMap(customizers);
    }

    /**
     * Sets the {@link Sprite} that is used for particle entities.
     */
    public ParticleOptions sprite(final Sprite sprite) {
        return customize(PREFIX + "render-sprite", entity -> entity.get(RenderComponent.class).sprite = sprite);
    }

    /**
     * Sets multiple {@link Sprite}s that are randomly used for particle entities.
     */
    public ParticleOptions sprites(Sprite... sprites) {
        return customize(PREFIX + "render-sprite", entity -> entity.get(RenderComponent.class).sprite = ListUtil.randomFrom(sprites));
    }

    /**
     * Sets multiple {@link Sprite}s that are randomly used for particle entities.
     */
    public ParticleOptions sprites(Supplier<Sprite>... sprites) {
        return customize(PREFIX + "render-sprite", entity -> entity.get(RenderComponent.class).sprite = ListUtil.randomFrom(sprites).get());
    }

    /**
     * Sets the {@link Sprite} that is used for particle entities.
     */
    public ParticleOptions sprite(final Supplier<Sprite> sprite) {
        return sprite(sprite.get());
    }

    //TODO ParticleModifiers?
    public Collection<ParticleCustomizer> customizers() {
        return customizers.values();
    }

    public ParticleOptions tweenMode(final TweenMode tweenMode) {
        return customize(PREFIX + "tween-tweenmode", entity -> entity.get(TweenComponent.class).mode = tweenMode);
    }

    public ParticleOptions startScale(final double scale) {
        return customize(PREFIX + "render-scale", entity -> {
            final var render = entity.get(RenderComponent.class);
            render.options = render.options.scale(scale);
        });
    }

    public ParticleOptions randomStartScale(final double from, final double to) {
        return customize(PREFIX + "render-scale", entity -> {
            final var render = entity.get(RenderComponent.class);
            render.options = render.options.scale(RANDOM.nextDouble(from, to));
        });
    }

    public ParticleOptions animateOpacity() {
        return animateOpacity(Percent.zero(), Percent.max());
    }

    public ParticleOptions animateOpacity(final Percent from, final Percent to) {
        return customize(PREFIX + "render-opacity",
                entity -> entity.add(new TweenOpacityComponent(from, to)));
    }

    public ParticleOptions drawOrderIfMissing(final int drawOrder) {
        return customizers.containsKey(PREFIX + "render-draworder")
                ? this
                : drawOrder(drawOrder);
    }

    public ParticleOptions drawOrder(final int drawOrder) {
        return customize(PREFIX + "render-draworder",
                entity -> entity.get(RenderComponent.class).drawOrder = drawOrder);
    }

    public ParticleOptions baseMovement(final Vector speed) {
        return customize(PREFIX + "physics-movement", entity -> {
            entity.get(PhysicsComponent.class).momentum = speed;
            final var chaoticMovement = entity.get(ChaoticMovementComponent.class);
            if (nonNull(chaoticMovement)) {
                chaoticMovement.baseSpeed = speed;
            }
        });
    }

    public ParticleOptions chaoticMovement(final int speed, final Duration interval) {
        return customize(PREFIX + "chaoticmovement", entity -> {
            final var baseSpeed = entity.get(PhysicsComponent.class).momentum;
            entity.add(new ChaoticMovementComponent(speed, interval, baseSpeed));
        });
    }

    public ParticleOptions randomStartRotation() {
        return customize(PREFIX + "render-rotation", entity -> {
            final var render = entity.get(RenderComponent.class);
            render.options = render.options.rotation(Rotation.random());
        });
    }

    public ParticleOptions lifetimeSeconds(final long seconds) {
        return customize(PREFIX + "lifetime", entity -> entity.get(TweenComponent.class).duration = Duration.ofSeconds(seconds));
    }

    public ParticleOptions randomLifeTimeSeconds(final long from, final long to) {
        return customize(PREFIX + "lifetime", entity -> {
            final long minNanos = Duration.ofSeconds(from).nanos();
            final long maxNanos = Duration.ofSeconds(to).nanos();
            final long actualNanos = RANDOM.nextLong(minNanos, maxNanos);
            entity.get(TweenComponent.class).duration = Duration.ofNanos(actualNanos);
        });
    }

    public ParticleOptions animateScale(final double from, final double to) {
        return customize(PREFIX + "render-scale", entity -> entity.add(new TweenScaleComponent(from, to)));
    }

    /**
     * Add special customization to the {@link ParticleOptions} that is not already available via
     * {@link ParticleOptions} methods.
     */
    public ParticleOptions customize(final String identifier, final ParticleCustomizer customizer) {
        //TODO if identifier already present dont copy
        final Map<String, ParticleCustomizer> nextCustomizers = new HashMap<>();
        nextCustomizers.putAll(customizers);
        nextCustomizers.put(identifier, customizer);
        return new ParticleOptions(nextCustomizers);
    }

    /**
     * Returns a set of all registered customizer identifiers. These identifiers are used to replace
     * {@link ParticleCustomizer}s already added to the {@link ParticleCustomizer} instead of adding a duplicate
     * {@link ParticleCustomizer} with different settings. Identifiers added via {@link ParticleOptions} methods
     * start with 'default-' suffix.
     */
    public Set<String> customizerIdentifiers() {
        return customizers.keySet();
    }
}

package io.github.srcimon.screwbox.core.particles;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.environment.particles.ParticleEmitterComponent;
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
import java.util.List;
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

    private static final Random RANDOM = new Random();
    private static final String PREFIX = "default-";
    private static final String MOVEMENT_PREFIX = PREFIX + "physics-movement";
    private static final String LIFETIME_PREFIX = PREFIX + "tween-duration";
    private static final String SCALE_PREFIX = PREFIX + "render-scale";
    private static final String SPRITE_PREFIX = PREFIX + "render-sprite";

    /**
     * Creates a new instance without {@link #source()}.
     */
    public static ParticleOptions unknownSource() {
        return new ParticleOptions();
    }

    /**
     * Creates a new instance. Sets {@link #source()}.
     */
    public static ParticleOptions particleSource(final Entity source) {
        return new ParticleOptions(source);
    }

    public ParticleOptions startOpacity(final Percent opacity) {
        return customize(PREFIX + "start-render-opacity",
                entity -> entity.get(RenderComponent.class).options = entity.get(RenderComponent.class).options.opacity(opacity));
    }

    /**
     * Used to add special customization to the particles that is not already available via {@link ParticleOptions}
     * methods.
     *
     * @see #customize(String, ParticleModifiers)
     */
    @FunctionalInterface
    public interface ParticleModifiers extends Consumer<Entity>, Serializable {

    }

    private final Map<String, ParticleModifiers> modifiers;

    private final Entity source;

    private ParticleOptions() {
        this(null);
    }

    /**
     * Creates a new instance with {@link #source()}.
     */
    private ParticleOptions(Entity source) {
        this(source, new HashMap<>());
    }

    private ParticleOptions(final Entity source, final Map<String, ParticleModifiers> modifiers) {
        this.source = source;
        this.modifiers = Collections.unmodifiableMap(modifiers);
    }

    /**
     * Sets the {@link Sprite} that is used for particle entities.
     */
    public ParticleOptions sprite(final Sprite sprite) {
        return customize(SPRITE_PREFIX, entity -> entity.get(RenderComponent.class).sprite = sprite.freshInstance());
    }

    /**
     * Sets multiple {@link Sprite}s that are randomly used for particle entities.
     */
    public ParticleOptions sprites(List<Sprite> sprites) {
        return customize(SPRITE_PREFIX, entity -> entity.get(RenderComponent.class).sprite = ListUtil.randomFrom(sprites).freshInstance());
    }

    /**
     * Sets multiple {@link Sprite}s that are randomly used for particle entities.
     */
    public ParticleOptions sprites(Sprite... sprites) {
        return customize(SPRITE_PREFIX, entity -> entity.get(RenderComponent.class).sprite = ListUtil.randomFrom(sprites).freshInstance());
    }

    /**
     * Sets multiple {@link Sprite}s that are randomly used for particle entities.
     */
    @SafeVarargs
    public final ParticleOptions sprites(Supplier<Sprite>... sprites) {
        return customize(SPRITE_PREFIX, entity -> entity.get(RenderComponent.class).sprite = ListUtil.randomFrom(sprites).get());
    }

    /**
     * Sets the {@link Sprite} that is used for particle entities.
     */
    public ParticleOptions sprite(final Supplier<Sprite> sprite) {
        return sprite(sprite.get());
    }

    /**
     * Returns all modifiers used for particle entities.
     */
    public Collection<ParticleModifiers> modifiers() {
        return modifiers.values();
    }

    /**
     * Sets the {@link TweenMode} for animation.
     */
    public ParticleOptions tweenMode(final TweenMode tweenMode) {
        return customize(PREFIX + "tween-tweenmode", entity -> entity.get(TweenComponent.class).mode = tweenMode);
    }

    /**
     * Sets the initial scale of the particle.
     */
    public ParticleOptions startScale(final double scale) {
        return customize(SCALE_PREFIX, entity -> {
            final var render = entity.get(RenderComponent.class);
            render.options = render.options.scale(scale);
        });
    }

    /**
     * Sets the initial scale of the particle to a random number in the given range.
     */
    public ParticleOptions randomStartScale(final double from, final double to) {
        return customize(SCALE_PREFIX, entity -> {
            final var render = entity.get(RenderComponent.class);
            render.options = render.options.scale(RANDOM.nextDouble(from, to));
        });
    }

    /**
     * Adds an {@link TweenOpacityComponent} to the particle to animate opacity.
     */
    public ParticleOptions animateOpacity() {
        return animateOpacity(Percent.zero(), Percent.max());
    }

    /**
     * Adds an {@link TweenOpacityComponent} to the particle to animate opacity.
     */
    public ParticleOptions animateOpacity(final Percent from, final Percent to) {
        return customize(PREFIX + "render-opacity",
                entity -> entity.add(new TweenOpacityComponent(from, to)));
    }

    /**
     * Sets the draw order of the particle when drawn. If not set the order of the {@link #source()} will be used
     * if present.
     */
    public ParticleOptions drawOrder(final int drawOrder) {
        return customize(PREFIX + "render-draworder",
                entity -> entity.get(RenderComponent.class).drawOrder = drawOrder);
    }

    /**
     * Adds an initial movement to the particle.
     */
    public ParticleOptions baseSpeed(final Vector speed) {
        return customize(MOVEMENT_PREFIX, entity -> {
            entity.get(PhysicsComponent.class).momentum = speed;
            final var chaoticMovement = entity.get(ChaoticMovementComponent.class);
            if (nonNull(chaoticMovement)) {
                chaoticMovement.baseSpeed = speed;
            }
        });
    }

    /**
     * Adds an random initial movement to the particle.
     */
    public ParticleOptions randomBaseSpeed(final double speed) {
        return customize(MOVEMENT_PREFIX, entity -> {
            final Vector speedVector = Vector.random(speed);
            entity.get(PhysicsComponent.class).momentum = speedVector;
            final var chaoticMovement = entity.get(ChaoticMovementComponent.class);
            if (nonNull(chaoticMovement)) {
                chaoticMovement.baseSpeed = speedVector;
            }
        });
    }

    /**
     * Adds an random initial movement to the particle.
     */
    public ParticleOptions randomBaseSpeed(final double from, final double to) {
        return customize(MOVEMENT_PREFIX, entity -> {
            final Vector speed = Vector.random(RANDOM.nextDouble(from, to));
            entity.get(PhysicsComponent.class).momentum = speed;
            final var chaoticMovement = entity.get(ChaoticMovementComponent.class);
            if (nonNull(chaoticMovement)) {
                chaoticMovement.baseSpeed = speed;
            }
        });
    }

    /**
     * Adds chaotic movement to the particle.
     */
    public ParticleOptions chaoticMovement(final double speed, final Duration interval) {
        return customize(PREFIX + "chaoticmovement", entity -> {
            final var baseSpeed = entity.get(PhysicsComponent.class).momentum;
            entity.add(new ChaoticMovementComponent(speed, interval, baseSpeed));
        });
    }

    /**
     * Adds chaotic movement to the particle.
     */
    public ParticleOptions chaoticMovement(final double speed, final Duration interval, final Vector baseSpeed) {
        return customize(PREFIX + "chaoticmovement", entity -> entity.add(new ChaoticMovementComponent(speed, interval, baseSpeed)));
    }

    /**
     * Sets the initial {@link Rotation} of a particle to a random value.
     */
    public ParticleOptions randomStartRotation() {
        return customize(PREFIX + "render-rotation", entity -> {
            final var render = entity.get(RenderComponent.class);
            render.options = render.options.rotation(Rotation.random());
        });
    }

    /**
     * Sets the particle lifetime in seconds.
     */
    public ParticleOptions lifetimeSeconds(final long seconds) {
        return customize(LIFETIME_PREFIX, entity -> entity.get(TweenComponent.class).duration = Duration.ofSeconds(seconds));
    }

    /**
     * Sets the particle lifetime in milliseconds.
     */
    public ParticleOptions lifetimeMilliseconds(final long milliseconds) {
        return customize(LIFETIME_PREFIX, entity -> entity.get(TweenComponent.class).duration = Duration.ofMillis(milliseconds));
    }

    /**
     * Sets the particle lifetime to a random amount of seconts in the given range.
     */
    public ParticleOptions randomLifeTimeMilliseconds(final long from, final long to) {
        return customize(LIFETIME_PREFIX, entity -> {
            final long minNanos = Duration.ofMillis(from).nanos();
            final long maxNanos = Duration.ofMillis(to).nanos();
            final long actualNanos = RANDOM.nextLong(minNanos, maxNanos);
            entity.get(TweenComponent.class).duration = Duration.ofNanos(actualNanos);
        });
    }

    /**
     * Sets the particle lifetime to a random amount of seconts in the given range.
     */
    public ParticleOptions randomLifeTimeSeconds(final long from, final long to) {
        return customize(LIFETIME_PREFIX, entity -> {
            final long minNanos = Duration.ofSeconds(from).nanos();
            final long maxNanos = Duration.ofSeconds(to).nanos();
            final long actualNanos = RANDOM.nextLong(minNanos, maxNanos);
            entity.get(TweenComponent.class).duration = Duration.ofNanos(actualNanos);
        });
    }

    /**
     * Adds a {@link TweenScaleComponent} to a particle to animate the scale of the particle.
     */
    public ParticleOptions animateScale(final double from, final double to) {
        return customize(SCALE_PREFIX, entity -> entity.add(new TweenScaleComponent(from, to)));
    }

    /**
     * Add special customization to the {@link ParticleOptions} that is not already available via
     * {@link ParticleOptions} methods.
     */
    public ParticleOptions customize(final String identifier, final ParticleModifiers modifier) {
        final Map<String, ParticleModifiers> nextCustomizers = new HashMap<>(modifiers);
        nextCustomizers.put(identifier, modifier);
        if (nextCustomizers.size() > 100) {
            throw new IllegalStateException("added more than 100 modifiers. This is most likely a programming error. use identifiers to overwrite existing modifiers");
        }
        return new ParticleOptions(source, nextCustomizers);
    }

    /**
     * Returns a set of all registered modifier identifiers. These identifiers are used to replace
     * {@link ParticleModifiers}s already added to the {@link ParticleModifiers} instead of adding a duplicate
     * {@link ParticleModifiers} with different settings. Identifiers added via {@link ParticleOptions} methods
     * start with 'default-' suffix.
     */
    public Set<String> modifierIds() {
        return modifiers.keySet();
    }

    /**
     * Sets the source {@link Entity} of the particle. The source will be used to add draw order if not set manully.
     */
    public ParticleOptions source(final Entity source) {
        return new ParticleOptions(source, modifiers);
    }

    /**
     * Returns the source {@link Entity} of this particle.
     */
    public Entity source() {
        return source;
    }
}

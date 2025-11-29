package dev.screwbox.core.particles;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Duration;
import dev.screwbox.core.Ease;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.light.OccluderComponent;
import dev.screwbox.core.environment.particles.ParticleEmitterComponent;
import dev.screwbox.core.environment.physics.ChaoticMovementComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.FixedRotationComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.environment.tweening.TweenComponent;
import dev.screwbox.core.environment.tweening.TweenOpacityComponent;
import dev.screwbox.core.environment.tweening.TweenScaleComponent;
import dev.screwbox.core.environment.tweening.TweenSpinComponent;
import dev.screwbox.core.graphics.ShaderSetup;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.utils.ListUtil;

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
import static java.util.Objects.requireNonNull;

/**
 * Is used by the {@link ParticleEmitterComponent} to create {@link Environment#entities()} that are used for particle effects.
 *
 * @see Environment#enableParticles()
 */
public class ParticleOptions implements Serializable {

    private static final Random RANDOM = new Random();
    private static final String PREFIX = "default-";
    private static final String MOVEMENT_PREFIX = PREFIX + "physics-movement";
    private static final String LIFESPAN_PREFIX = PREFIX + "lifespan";
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
     * Adds continuous rotation to the {@link Sprite}.
     */
    public ParticleOptions randomRotation(final double speed) {
        return customize(PREFIX + "sprite-rotation", entity -> entity.add(new FixedRotationComponent(speed)));
    }

    /**
     * Adds random continuous rotation to the {@link Sprite}.
     */
    public ParticleOptions randomRotation(final double minSpeed, final double maxSpeed) {
        return customize(PREFIX + "sprite-rotation", entity -> {
            final var speed = RANDOM.nextDouble(minSpeed, maxSpeed);
            entity.add(new FixedRotationComponent(speed));
        });
    }

    /**
     * Used to add special customization to the particles that is not already available via {@link ParticleOptions}
     * methods.
     *
     * @see #customize(String, ParticleModifier)
     */
    @FunctionalInterface
    public interface ParticleModifier extends Consumer<Entity>, Serializable {

    }

    private final Map<String, ParticleModifier> modifiers;
    private final Entity source;
    private int relativeDrawOrder;

    private ParticleOptions() {
        this(null);
    }

    /**
     * Creates a new instance with {@link #source()}.
     */
    private ParticleOptions(final Entity source) {
        this(source, new HashMap<>(), 0);
    }

    private ParticleOptions(final Entity source, final Map<String, ParticleModifier> modifiers, final int relativeDrawOrder) {
        this.source = source;
        this.modifiers = Collections.unmodifiableMap(modifiers);
        this.relativeDrawOrder = relativeDrawOrder;
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
    public Collection<ParticleModifier> modifiers() {
        return modifiers.values();
    }

    /**
     * Adds a horizontal spin via {@link TweenSpinComponent} to the particle entities.
     */
    public ParticleOptions animateHorizontalSpin() {
        return customize(PREFIX + "spin", entity -> entity.add(new TweenSpinComponent()));
    }

    /**
     * Adds a vertical spin via {@link TweenSpinComponent} to the particle entities.
     */
    public ParticleOptions animateVerticalSpin() {
        return customize(PREFIX + "spin", entity -> entity.add(new TweenSpinComponent(false)));
    }

    /**
     * Sets the {@link Ease} for animation.
     */
    public ParticleOptions ease(final Ease ease) {
        return customize(PREFIX + "tween-ease", entity -> entity.get(TweenComponent.class).mode = ease);
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
     * Sets the {@link ShaderSetup} for the particle.
     *
     * @since 2.16.0
     */
    public ParticleOptions shaderSetup(final Supplier<ShaderSetup> shaderSetup) {
        return shaderSetup(shaderSetup.get());
    }

    /**
     * Makes the offset for the {@link ShaderSetup} for the particle random.
     * Requires a already set {@link ShaderSetup}
     *
     * @see #shaderSetup(ShaderSetup)
     * @since 2.17.0
     */
    public ParticleOptions randomShaderOffset() {
        return customize(PREFIX + "-shader-offset", entity -> {
            final var render = entity.get(RenderComponent.class);
            ShaderSetup currentSetup = render.options.shaderSetup();
            requireNonNull(currentSetup, "shader setup is null");
            render.options = render.options.shaderSetup(currentSetup.randomOffset());
        });
    }

    /**
     * Sets the {@link ShaderSetup} for the particle.
     *
     * @since 2.16.0
     */
    public ParticleOptions shaderSetup(final ShaderSetup shaderSetup) {
        return customize(PREFIX + "-shader", entity -> {
            final var render = entity.get(RenderComponent.class);
            render.options = render.options.shaderSetup(shaderSetup);
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
        return customize(PREFIX + "render-drawOrder",
                entity -> {
                    final var renderComponent = entity.get(RenderComponent.class);
                    renderComponent.options = renderComponent.options.drawOrder(drawOrder);
                });
    }

    /**
     * Adds an initial movement to the particle.
     */
    public ParticleOptions baseSpeed(final Vector speed) {
        return customize(MOVEMENT_PREFIX, entity -> {
            entity.get(PhysicsComponent.class).velocity = speed;
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
            entity.get(PhysicsComponent.class).velocity = speedVector;
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
            entity.get(PhysicsComponent.class).velocity = speed;
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
        return customize(PREFIX + "chaoticMovement", entity -> {
            final var baseSpeed = entity.get(PhysicsComponent.class).velocity;
            entity.add(new ChaoticMovementComponent(speed, interval, baseSpeed));
        });
    }

    /**
     * Adds chaotic movement to the particle.
     */
    public ParticleOptions chaoticMovement(final double speed, final Duration interval, final Vector baseSpeed) {
        return customize(PREFIX + "chaoticMovement", entity -> entity.add(new ChaoticMovementComponent(speed, interval, baseSpeed)));
    }

    /**
     * Sets the initial {@link Angle} of a particle to a random value.
     */
    public ParticleOptions randomStartRotation() {
        return customize(PREFIX + "render-rotation", entity -> {
            final var render = entity.get(RenderComponent.class);
            render.options = render.options.rotation(Angle.random());
        });
    }

    /**
     * Sets the particle lifespan in seconds.
     */
    public ParticleOptions lifespanSeconds(final long seconds) {
        return customize(LIFESPAN_PREFIX, entity -> entity.get(TweenComponent.class).duration = Duration.ofSeconds(seconds));
    }

    /**
     * Sets the particle lifespan in milliseconds.
     */
    public ParticleOptions lifespanMilliseconds(final long milliseconds) {
        return customize(LIFESPAN_PREFIX, entity -> entity.get(TweenComponent.class).duration = Duration.ofMillis(milliseconds));
    }

    /**
     * Sets the particle lifespan to a random amount of seconds in the given range.
     */
    public ParticleOptions randomLifespanMilliseconds(final long from, final long to) {
        return customize(LIFESPAN_PREFIX, entity -> {
            final long minNanos = Duration.ofMillis(from).nanos();
            final long maxNanos = Duration.ofMillis(to).nanos();
            final long actualNanos = RANDOM.nextLong(minNanos, maxNanos);
            entity.get(TweenComponent.class).duration = Duration.ofNanos(actualNanos);
        });
    }

    /**
     * Sets the particle lifespan to a random amount of seconds in the given range.
     */
    public ParticleOptions randomLifespanSeconds(final long from, final long to) {
        return customize(LIFESPAN_PREFIX, entity -> {
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
     * Adds a {@link OccluderComponent} to the particle.
     */
    public ParticleOptions occlude() {
        return customize("occlude", entity -> entity.add(new OccluderComponent(false)));
    }

    /**
     * Add special customization to the {@link ParticleOptions} that is not already available via
     * {@link ParticleOptions} methods.
     */
    public ParticleOptions customize(final String identifier, final ParticleModifier modifier) {
        final Map<String, ParticleModifier> nextCustomizers = new HashMap<>(modifiers);
        nextCustomizers.put(identifier, modifier);
        if (nextCustomizers.size() > 100) {
            throw new IllegalStateException("added more than 100 modifiers. This is most likely a programming error. use identifiers to overwrite existing modifiers");
        }
        return new ParticleOptions(source, nextCustomizers, relativeDrawOrder);
    }

    /**
     * Returns a set of all registered modifier identifiers. These identifiers are used to replace
     * {@link ParticleModifier}s already added to the {@link ParticleModifier} instead of adding a duplicate
     * {@link ParticleModifier} with different settings. Identifiers added via {@link ParticleOptions} methods
     * start with 'default-' suffix.
     */
    public Set<String> modifierIds() {
        return modifiers.keySet();
    }

    /**
     * Sets the source {@link Entity} of the particle. The source will be used to add draw order if not set manually.
     */
    public ParticleOptions source(final Entity source) {
        return new ParticleOptions(source, modifiers, relativeDrawOrder);
    }

    /**
     * Returns the source {@link Entity} of this particle.
     */
    public Entity source() {
        return source;
    }

    /**
     * Changes the relative draw order of the spawned sprites when specifying a {@link #source()}. Default is zero.
     *
     * @since 3.3.0
     */
    public ParticleOptions relativeDrawOrder(final int relativeDrawOrder) {
        this.relativeDrawOrder = relativeDrawOrder;
        return this;
    }

    /**
     * Returns the relative draw order of the spawned sprites when specifying a {@link #source()}. Default is zero.
     *
     * @since 3.3.0
     */
    public int relativeDrawOrder() {
        return relativeDrawOrder;
    }
}
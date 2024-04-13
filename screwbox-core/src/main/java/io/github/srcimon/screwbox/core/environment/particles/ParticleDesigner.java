package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.assets.SpritesBundle;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.physics.ChaoticMovementComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenDestroyComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenMode;
import io.github.srcimon.screwbox.core.environment.tweening.TweenOpacityComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenScaleComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.utils.ListUtil;

import java.io.Serializable;
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
public class ParticleDesigner implements Serializable {

    private static final Random RANDOM = new Random();
    private static final String PREFIX = "default-";

    /**
     * Used to add special customization to the particles that is not already available via {@link ParticleDesigner}
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
    public ParticleDesigner() {
        this(new HashMap<>());
    }

    private ParticleDesigner(final Map<String, ParticleCustomizer> customizers) {
        this.customizers = Collections.unmodifiableMap(customizers);
    }

    /**
     * Sets the {@link Sprite} that is used for particle entities.
     */
    public ParticleDesigner sprite(final Sprite sprite) {
        return customize(PREFIX + "render-sprite", entity -> entity.get(RenderComponent.class).sprite = sprite);
    }

    /**
     * Sets multiple {@link Sprite}s that are randomly used for particle entities.
     */
    public ParticleDesigner sprites(Sprite... sprites) {
        return customize(PREFIX + "render-sprite", entity -> entity.get(RenderComponent.class).sprite = ListUtil.randomFrom(sprites));
    }

    /**
     * Sets multiple {@link Sprite}s that are randomly used for particle entities.
     */
    public ParticleDesigner sprites(Supplier<Sprite>... sprites) {
        return customize(PREFIX + "render-sprite", entity -> entity.get(RenderComponent.class).sprite = ListUtil.randomFrom(sprites).get());
    }

    /**
     * Sets the {@link Sprite} that is used for particle entities.
     */
    public ParticleDesigner sprite(final Supplier<Sprite> sprite) {
        return sprite(sprite.get());
    }


    public Entity createParticle(final Vector position, final int drawOrder) { // TODO fix order can also be set via customizer
        var physicsComponent = new PhysicsComponent();
        physicsComponent.ignoreCollisions = true;
        physicsComponent.gravityModifier = 0;
        physicsComponent.magnetModifier = 0;
        TransformComponent transfrom = new TransformComponent(position, 1, 1);
        RenderComponent render = new RenderComponent(SpritesBundle.DOT_BLUE_16, drawOrder, SpriteDrawOptions.originalSize());
        final var entity = new Entity();
        entity.add(new ParticleComponent());
        entity.add(new TweenComponent(Duration.ofSeconds(1), TweenMode.LINEAR_IN));
        entity.add(new TweenDestroyComponent());
        entity.add(physicsComponent);
        entity.add(transfrom);
        entity.add(render);
        for (final var entityCustomizer : customizers.values()) {
            entityCustomizer.accept(entity);
        }
        transfrom.bounds = Bounds.atPosition(position, render.sprite.size().width() * render.options.scale(), render.sprite.size().height() * render.options.scale());
        return entity;

    }

    public ParticleDesigner tweenMode(final TweenMode tweenMode) {
        return customize(PREFIX + "tween-tweenmode", entity -> entity.get(TweenComponent.class).mode = tweenMode);
    }

    public ParticleDesigner startScale(final double scale) {
        return customize(PREFIX + "render-scale", entity -> {
            final var render = entity.get(RenderComponent.class);
            render.options = render.options.scale(scale);
        });
    }

    public ParticleDesigner randomStartScale(final double from, final double to) {
        return customize(PREFIX + "render-scale", entity -> {
            final var render = entity.get(RenderComponent.class);
            render.options = render.options.scale(RANDOM.nextDouble(from, to));
        });
    }

    public ParticleDesigner animateOpacity() {
        return animateOpacity(Percent.zero(), Percent.max());
    }

    public ParticleDesigner animateOpacity(final Percent from, final Percent to) {
        return customize(PREFIX + "render-opacity",
                entity -> entity.add(new TweenOpacityComponent(from, to)));
    }

    public ParticleDesigner drawOrder(final int drawOrder) {
        return customize(PREFIX + "render-draworder",
                entity -> entity.get(RenderComponent.class).drawOrder = drawOrder);
    }

    public ParticleDesigner baseMovement(final Vector speed) {
        return customize(PREFIX + "physics-movement", entity -> {
            entity.get(PhysicsComponent.class).momentum = speed;
            final var chaoticMovement = entity.get(ChaoticMovementComponent.class);
            if (nonNull(chaoticMovement)) {
                chaoticMovement.baseSpeed = speed;
            }
        });
    }

    public ParticleDesigner chaoticMovement(final int speed, final Duration interval) {
        return customize(PREFIX + "chaoticmovement", entity -> {
            final var baseSpeed = entity.get(PhysicsComponent.class).momentum;
            entity.add(new ChaoticMovementComponent(speed, interval, baseSpeed));
        });
    }

    public ParticleDesigner randomStartRotation() {
        return customize(PREFIX + "render-rotation", entity -> {
            final var render = entity.get(RenderComponent.class);
            render.options = render.options.rotation(Rotation.random());
        });
    }

    public ParticleDesigner lifetimeSeconds(final long seconds) {
        return customize(PREFIX + "lifetime", entity -> entity.get(TweenComponent.class).duration = Duration.ofSeconds(seconds));
    }

    public ParticleDesigner randomLifeTimeSeconds(final long from, final long to) {
        return customize(PREFIX + "lifetime", entity -> {
            final long minNanos = Duration.ofSeconds(from).nanos();
            final long maxNanos = Duration.ofSeconds(to).nanos();
            final long actualNanos = RANDOM.nextLong(minNanos, maxNanos);
            entity.get(TweenComponent.class).duration = Duration.ofNanos(actualNanos);
        });
    }

    public ParticleDesigner animateScale(final double from, final double to) {
        return customize(PREFIX + "render-scale", entity -> entity.add(new TweenScaleComponent(from, to)));
    }

    /**
     * Add special customization to the {@link ParticleDesigner} that is not already available via
     * {@link ParticleDesigner} methods.
     */
    public ParticleDesigner customize(final String identifier, final ParticleCustomizer customizer) {
        //TODO if identifier already present dont copy
        final Map<String, ParticleCustomizer> nextCustomizers = new HashMap<>();
        nextCustomizers.putAll(customizers);
        nextCustomizers.put(identifier, customizer);
        return new ParticleDesigner(nextCustomizers);
    }

    /**
     * Returns a set of all registered customizer identifiers. These identifiers are used to replace
     * {@link ParticleCustomizer}s already added to the {@link ParticleCustomizer} instead of adding a duplicate
     * {@link ParticleCustomizer} with different settings. Identifiers added via {@link ParticleDesigner} methods
     * start with 'default-' suffix.
     */
    public Set<String> customizerIdentifiers() {
        return customizers.keySet();
    }
}

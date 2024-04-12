package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.assets.SpritesBundle;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.physics.ChaoticMovementComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenDestroyComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenMode;
import io.github.srcimon.screwbox.core.environment.tweening.TweenOpacityComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.utils.ListUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.util.Objects.nonNull;

//TODO addMethod seal()
//TODO ParticleBundle.SMOKE
public class ParticleDesigner implements Serializable {

    private static final Random RANDOM = new Random();

    private boolean isSealed = false;
    public ParticleDesigner seal() {
        isSealed = true;
        return this;
    }

    @FunctionalInterface
    public interface ParticleCustomizer extends Consumer<Entity>, Serializable {

    }

    public boolean isSealed() {
        return isSealed;
    }
    public ParticleDesigner customize(final String identifier, final ParticleCustomizer customizer) {
        if(isSealed) {
            throw new IllegalStateException("particle desinger was sealed so it cannot be customized anymore");
        }
        customizers.put(identifier, customizer);
        return this;
    }

    //TODO replace with entitycustomizer

    private final Map<String, ParticleCustomizer> customizers = new HashMap<>();

    public ParticleDesigner sprite(final Sprite sprite) {
        customizers.put("default-sprite", entity -> entity.get(RenderComponent.class).sprite = sprite);
        return this;
    }

    public ParticleDesigner sprite(final Supplier<Sprite> sprite) {
        return sprite(sprite.get());
    }

    //TODO: multipleSpriteSupport

    public Entity createEntity(final Vector position) {
        var physicsComponent = new PhysicsComponent(Vector.y(-100));
        physicsComponent.ignoreCollisions = true;
        TransformComponent transfrom = new TransformComponent(position, 1, 1);
        RenderComponent render = new RenderComponent(SpritesBundle.PARTICLE_16, 0, SpriteDrawOptions.originalSize());
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
        customize("default-tweenMode", entity -> entity.get(TweenComponent.class).mode = tweenMode);
        return this;
    }

    public ParticleDesigner startScale(final double scale) {
        customize("default-startScale", entity -> {
            final var render = entity.get(RenderComponent.class);
            render.options = render.options.scale(scale);
        });
        return this;
    }

    public ParticleDesigner randomStartScale(final double from, final double to) {
        //TODO validate from to size
        customize("default-randomStartScale", entity -> {
            final var render = entity.get(RenderComponent.class);
            render.options = render.options.scale(RANDOM.nextDouble(from, to));
        });
        return this;
    }

    public ParticleDesigner animateOpacity() {
        return animateOpacity(Percent.zero(), Percent.max());
    }

    public ParticleDesigner animateOpacity(final Percent from, final Percent to) {
        //TODO validate from to size
        customize("default-animateOpacity",
                entity -> entity.add(new TweenOpacityComponent(from, to)));
        return this;
    }

    public ParticleDesigner drawOrder(final int drawOrder) {
        customize("default-drawOrder",
                entity -> entity.get(RenderComponent.class).drawOrder = drawOrder);
        return this;
    }

    public ParticleDesigner baseMovement(final Vector speed) {
        customize("default-XXXX", entity -> {
            entity.get(PhysicsComponent.class).momentum = speed;
            final var chaoticMovement = entity.get(ChaoticMovementComponent.class);
            if (nonNull(chaoticMovement)) {
                chaoticMovement.baseSpeed = speed;
            }
        });
        return this;
    }

    public ParticleDesigner chaoticMovement(final int speed, final Duration interval) {
        customize("default-chaoticMovement", entity -> {
            final var baseSpeed = entity.get(PhysicsComponent.class).momentum;
            entity.add(new ChaoticMovementComponent(speed, interval, baseSpeed));
        });
        return this;
    }

    public ParticleDesigner randomStartRotation() {
        customize("default-randomStartRotation", entity -> {
            final var render = entity.get(RenderComponent.class);
            render.options = render.options.rotation(Rotation.random());
        });
        return this;
    }

    public ParticleDesigner lifetimeSeconds(final long seconds) {
        customize("default-lifetimeSeconds", entity ->  entity.get(TweenComponent.class).duration = Duration.ofSeconds(seconds));
        return this;
    }

    public ParticleDesigner randomLifeTimeSeconds(final long from, final long to) {
        customize("default-randomLifeTimeSeconds", entity ->  {
            final long minNanos = Duration.ofSeconds(from).nanos();
            final long maxNanos = Duration.ofSeconds(to).nanos();
            final  long actualNanos = RANDOM.nextLong(minNanos, maxNanos);
            entity.get(TweenComponent.class).duration = Duration.ofNanos(actualNanos);
        });
        return this;
    }
}

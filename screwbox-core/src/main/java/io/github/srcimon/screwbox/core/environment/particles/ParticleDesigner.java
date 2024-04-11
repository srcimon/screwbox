package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.Vector;
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
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.util.Objects.nonNull;

//TODO addMethod seal()
//TODO ParticleBundle.SMOKE
public class ParticleDesigner implements Serializable {

    private static final Random RANDOM = new Random();

    @FunctionalInterface
    public interface ParticleCustomizer extends Consumer<Entity>, Serializable {

    }

    public ParticleDesigner customize(final ParticleCustomizer customizer) {
        customizers.add(customizer);
        return this;
    }

    //TODO replace with entitycustomizer

    private final List<ParticleCustomizer> customizers = new ArrayList<>();

    private final List<Sprite> templates;

    public static ParticleDesigner useTemplate(final Sprite template) {
        return useTemplates(List.of(template));
    }

    public static ParticleDesigner useTemplate(final Supplier<Sprite> template) {
        return useTemplate(template.get());
    }

    public static ParticleDesigner useTemplates(final List<Sprite> templates) {
        return new ParticleDesigner(templates);
    }

    private ParticleDesigner(final List<Sprite> templates) {
        this.templates = templates;
    }

    public Entity createEntity(final Vector position) {
        var physicsComponent = new PhysicsComponent();
        physicsComponent.ignoreCollisions = true;
        Sprite sprite = ListUtil.randomFrom(templates);
        TransformComponent transfrom = new TransformComponent(position, 1, 1);
        RenderComponent render = new RenderComponent(sprite, 0, SpriteDrawOptions.originalSize());
        final var entity = new Entity();
        entity.add(new ParticleComponent());
        entity.add(new TweenComponent(Duration.ofSeconds(1), TweenMode.LINEAR_IN));
        entity.add(new TweenDestroyComponent());
        entity.add(physicsComponent);
        entity.add(transfrom);
        entity.add(render);
        for (final var entityCustomizer : customizers) {
            entityCustomizer.accept(entity);
        }
        transfrom.bounds = Bounds.atPosition(position, render.sprite.size().width() * render.options.scale(), render.sprite.size().height() * render.options.scale());
        return entity;

    }

    public ParticleDesigner tweenMode(final TweenMode tweenMode) {
        customize(entity -> entity.get(TweenComponent.class).mode = tweenMode);
        return this;
    }

    public ParticleDesigner startScale(final double scale) {
        customize(entity -> {
            final var render = entity.get(RenderComponent.class);
            render.options = render.options.scale(scale);
        });
        return this;
    }

    public ParticleDesigner randomStartScale(final double from, final double to) {
        //TODO validate from to size
        customize(entity -> {
            final var render = entity.get(RenderComponent.class);
            render.options = render.options.scale(RANDOM.nextDouble(from, to));
        });
        return this;
    }

    public ParticleDesigner animateOpacity(final Percent from, final Percent to) {
        //TODO validate from to size
        customize(entity -> entity.add(new TweenOpacityComponent(from, to)));
        return this;
    }

    public ParticleDesigner drawOrder(final int drawOrder) {
        customize(entity -> entity.get(RenderComponent.class).drawOrder = drawOrder);
        return this;
    }

    public ParticleDesigner baseMovement(final Vector speed) {
        customize(entity -> {
            entity.get(PhysicsComponent.class).momentum = speed;
            final var chaoticMovement = entity.get(ChaoticMovementComponent.class);
            if (nonNull(chaoticMovement)) {
                chaoticMovement.baseSpeed = speed;
            }
        });
        return this;
    }

    public ParticleDesigner chaoticMovement(final int speed, final Duration interval) {
        customize(entity -> {
            final var baseSpeed = entity.get(PhysicsComponent.class).momentum;
            entity.add(new ChaoticMovementComponent(speed, interval, baseSpeed));
        });
        return this;
    }

    public ParticleDesigner randomStartRotation() {
        customize(entity -> {
            final var render = entity.get(RenderComponent.class);
            render.options = render.options.rotation(Rotation.random());
        });
        return this;
    }

    public ParticleDesigner lifetimeSeconds(final long seconds) {
        customize(entity ->  entity.get(TweenComponent.class).duration = Duration.ofSeconds(seconds));
        return this;
    }

    public ParticleDesigner randomLifeTimeSeconds(final long from, final long to) {
        customize(entity ->  {
            final long minNanos = Duration.ofSeconds(from).nanos();
            final long maxNanos = Duration.ofSeconds(to).nanos();
            final  long actualNanos = RANDOM.nextLong(minNanos, maxNanos);
            entity.get(TweenComponent.class).duration = Duration.ofNanos(actualNanos);
        });
        return this;
    }
}

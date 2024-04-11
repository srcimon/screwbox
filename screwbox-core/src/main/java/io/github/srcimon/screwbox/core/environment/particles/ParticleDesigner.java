package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
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
import java.util.function.UnaryOperator;

public class ParticleDesigner implements Serializable {

    private static final Random RANDOM = new Random();

    //TODO replace with entitycustomizer
    private interface ParticleEntityCustomizer extends Consumer<Entity>, Serializable {

    }

    private final List<ParticleEntityCustomizer> entityCustomizers = new ArrayList<>();

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
        final var entity = new Entity();
        entity.add(new ParticleComponent());
        entity.add(new TweenComponent(Duration.ofSeconds(1), TweenMode.LINEAR_IN));
        entity.add(new TweenDestroyComponent());
        entity.add(new TransformComponent(position, 1, 1));
        entity.add(new RenderComponent(sprite, 0, SpriteDrawOptions.originalSize()));
        for (final var entityCustomizer : entityCustomizers) {
            entityCustomizer.accept(entity);
        }
        return entity;

    }

    public ParticleDesigner tweenMode(final TweenMode tweenMode) {
        entityCustomizers.add(entity -> entity.get(TweenComponent.class).mode = tweenMode);
        return this;
    }

    public ParticleDesigner startScale(final double scale) {
        entityCustomizers.add(entity -> {
            final var render = entity.get(RenderComponent.class);
            render.options = render.options.scale(scale);
        });
        return this;
    }

    public ParticleDesigner randomStartScale(final double from, final double to) {
        //TODO validate from to size
        entityCustomizers.add(entity -> {
            final var render = entity.get(RenderComponent.class);
            render.options = render.options.scale(RANDOM.nextDouble(from, to));
        });
        return this;
    }

    public ParticleDesigner animateOpacity(final Percent from, final Percent to) {
        //TODO validate from to size
        entityCustomizers.add(entity -> entity.add(new TweenOpacityComponent(from, to)));
        return this;
    }

    public ParticleDesigner drawOrder(final int drawOrder) {
        entityCustomizers.add(entity -> entity.get(RenderComponent.class).drawOrder = drawOrder);
        return this;
    }
}

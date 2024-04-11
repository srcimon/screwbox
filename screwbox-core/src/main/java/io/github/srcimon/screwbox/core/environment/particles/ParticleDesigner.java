package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenDestroyComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenMode;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.utils.ListUtil;

import java.io.Serializable;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public record ParticleDesigner(
        List<Sprite> templates,
        Duration lifetime,
        TweenMode tweenMode,
        int drawOrder,
        double startScale
) implements Serializable {

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
        this(templates, Duration.ofSeconds(2), TweenMode.LINEAR_IN, 0, 1);
    }

    public Entity createEntity(final Vector position) {
        var physicsComponent = new PhysicsComponent();
        physicsComponent.ignoreCollisions = true;
        Sprite sprite = ListUtil.randomFrom(templates);
        return new Entity()
                .add(new TweenComponent(lifetime, tweenMode))
                .add(new TweenDestroyComponent())
                .add(new ParticleComponent())
                .add(new TransformComponent(position, 1, 1))
                .add(new RenderComponent(sprite, drawOrder, SpriteDrawOptions.scaled(startScale)));

    }

    public ParticleDesigner tweenMode(final TweenMode tweenMode) {
        return new ParticleDesigner(templates, lifetime, tweenMode, drawOrder, startScale);
    }

    public ParticleDesigner startScale(final double startScale) {
        return new ParticleDesigner(templates, lifetime, tweenMode, drawOrder, startScale);
    }
}

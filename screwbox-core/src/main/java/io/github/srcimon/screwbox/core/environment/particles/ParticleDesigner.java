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

public class ParticleDesigner implements Serializable {

    private static final Random RANDOM = new Random();

    private final List<Sprite> templates;

    private Duration lifeTimeMin = Duration.ofSeconds(1);
    private Duration lifeTimeMax = Duration.ofSeconds(1);
    private TweenMode tweenMode = TweenMode.LINEAR_IN;
    private int drawOrder = 0;
    private double startScale = 1;

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
        Duration lifetime = Duration.ofNanos(RANDOM.nextLong(lifeTimeMin.nanos(), lifeTimeMax.nanos()));
        Sprite sprite = ListUtil.randomFrom(templates);
        return new Entity()
                .add(new TweenComponent(lifetime, tweenMode))
                .add(new TweenDestroyComponent())
                .add(new ParticleComponent())
                .add(new TransformComponent(position, 1, 1))
                .add(new RenderComponent(sprite, drawOrder, SpriteDrawOptions.scaled(startScale)));

    }
}

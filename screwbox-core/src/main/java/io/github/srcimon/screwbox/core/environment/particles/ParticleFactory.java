package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenDestroyComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenMode;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import static io.github.srcimon.screwbox.core.utils.ListUtil.randomFrom;

//TODO ParticleFactoryBundle

public class ParticleFactory implements Serializable {

    private static final Random RANDOM = new Random();

    private final List<Sprite> sprites;

    private double scale = 1;
    private boolean useRandomStartRotation = false;
    private Duration maxAge = Duration.ofSeconds(5);

    private final List<Supplier<Component>> particleCustomizers = new ArrayList<>();

    public static ParticleFactory template(final Sprite sprite) {
        return templates(List.of(sprite));
    }

    public static ParticleFactory templates(final List<Sprite> sprites) {
        return new ParticleFactory(sprites);
    }

    private ParticleFactory(final List<Sprite> sprites) {
        this.sprites = sprites;
    }
//TODO: passt die signatur?
    public ParticleFactory addCustomizer(Supplier<Component> componentSupplier) {
        particleCustomizers.add(componentSupplier);
        return this;
    }
    public Entity createParticle(final Vector position) {
        SpriteDrawOptions options = SpriteDrawOptions
                .scaled(scale);

        if (useRandomStartRotation) {
            options = options.rotation(Rotation.degrees(RANDOM.nextDouble() * 360));
        }

        PhysicsComponent physicsComponent = new PhysicsComponent();
        physicsComponent.ignoreCollisions = true;

        Entity particle = new Entity()
                .add(physicsComponent)
                .add(new TweenDestroyComponent())
                .add(new TweenComponent(maxAge, TweenMode.SINE_IN_OUT))
                .add(new ParticleComponent())
                .add(new TransformComponent(position, 1, 1))
                .add(new RenderComponent(randomFrom(sprites), options));

        for(var customizer : particleCustomizers) {
           particle.add(customizer.get());
        }
        return particle;
    }

    //TODO make immutable
    public ParticleFactory scale(double scale) {
        this.scale = scale;
        return this;
    }

    public ParticleFactory useRandomStartRotation() {
        useRandomStartRotation = true;
        return this;
    }

    public ParticleFactory maxAge(Duration duration) {
        this.maxAge = duration;
        return this;
    }
}

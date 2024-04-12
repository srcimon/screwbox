package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.assets.SpritesBundle;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenDestroyComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenScaleComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ParticleDesignerTest {

    private ParticleDesigner particleDesigner;

    @BeforeEach
    void setUp() {
        particleDesigner = new ParticleDesigner();
    }

    @Test
    void createEntity_noCustomizersAdded_createsBasicParticle() {
        var entity = particleDesigner.createEntity(Vector.$(10, 20), 12);

        assertThat(entity.hasComponent(ParticleComponent.class)).isTrue();
        assertThat(entity.hasComponent(PhysicsComponent.class)).isTrue();
        assertThat(entity.hasComponent(TransformComponent.class)).isTrue();
        assertThat(entity.hasComponent(RenderComponent.class)).isTrue();
        assertThat(entity.hasComponent(TweenComponent.class)).isTrue();
        assertThat(entity.hasComponent(TweenDestroyComponent.class)).isTrue();
        assertThat(entity.bounds()).isEqualTo(Bounds.$$(2, 12, 16, 16));

        var renderComponent = entity.get(RenderComponent.class);
        assertThat(renderComponent.drawOrder).isEqualTo(12);

        var physicsComponent = entity.get(PhysicsComponent.class);
        assertThat(physicsComponent.gravityModifier).isZero();
        assertThat(physicsComponent.magnetModifier).isZero();
        assertThat(physicsComponent.ignoreCollisions).isTrue();
    }

    @Test
    void customizerIdentifiers_noCustomization_isEmpty() {
        assertThat(particleDesigner.customizerIdentifiers()).isEmpty();
    }

    @Test
    void customizerIdentifiers_addedCustomization_containsIdentifiers() {
        var result = particleDesigner
                .animateOpacity()
                .customize("my-identifier", entity -> entity.add(new TweenScaleComponent(10, 20)));

        assertThat(result.customizerIdentifiers()).containsExactly("default-animateOpacity", "my-identifier");
    }

    @Test
    void sprite_setsSpriteForCreatedEntities() {
        var entity = particleDesigner
                .sprite(SpritesBundle.MOON_SURFACE_16)
                .createEntity(Vector.zero(), 3);

        assertThat(entity.get(RenderComponent.class).sprite).isEqualTo(SpritesBundle.MOON_SURFACE_16.get());
    }

}

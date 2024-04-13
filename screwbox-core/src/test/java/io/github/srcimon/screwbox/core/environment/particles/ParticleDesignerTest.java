package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.assets.SpritesBundle;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenDestroyComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenMode;
import io.github.srcimon.screwbox.core.environment.tweening.TweenScaleComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ParticleDesignerTest {

    private ParticleOptions particleDesigner;

    @BeforeEach
    void setUp() {
        particleDesigner = new ParticleOptions();
    }

    @Test
    void createEntity_noCustomizersAdded_createsBasicParticle() {
        var entity = particleDesigner.createParticle(Vector.$(10, 20), 12);

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
        var particle = particleDesigner
                .animateOpacity()
                .customize("my-identifier", entity -> entity.add(new TweenScaleComponent(10, 20)));

        assertThat(particle.customizerIdentifiers()).containsExactly("default-render-opacity", "my-identifier");
    }

    @Test
    void createParticle_multipleCustomizersWithSameIdentifier_onlyLastAddedCustomizerIsUsed() {
        var particle = particleDesigner
                .sprite(SpritesBundle.MOON_SURFACE_16)
                .sprite(SpritesBundle.MOON_SURFACE_16.get())
                .sprite(SpritesBundle.BLOB_ANIMATED_16)
                .sprites(SpritesBundle.DOT_BLUE_16)
                .sprites(SpritesBundle.DOT_BLUE_16.get())
                .createParticle(Vector.zero(), 3);

        assertThat(particle.get(RenderComponent.class).sprite).isEqualTo(SpritesBundle.DOT_BLUE_16.get());
    }

    @Test
    void sprite_setsSpriteUsedForParticleCreation() {
        var particle = particleDesigner
                .sprite(SpritesBundle.MOON_SURFACE_16)
                .createParticle(Vector.zero(), 3);

        assertThat(particle.get(RenderComponent.class).sprite).isEqualTo(SpritesBundle.MOON_SURFACE_16.get());
    }

    @Test
    void sprites_setsSpritesUsedForParticleCreation() {
        var particle = particleDesigner
                .sprites(SpritesBundle.MOON_SURFACE_16, SpritesBundle.DOT_YELLOW_16)
                .createParticle(Vector.zero(), 3);

        assertThat(List.of(SpritesBundle.MOON_SURFACE_16.get(), SpritesBundle.DOT_YELLOW_16.get()))
                .contains(particle.get(RenderComponent.class).sprite);
    }

    @Test
    void tweenMode_setsTweenModeUsedForParticleCreation() {
        var particle = particleDesigner
                .tweenMode(TweenMode.SINE_IN_OUT)
                .createParticle(Vector.zero(), 3);

        assertThat(particle.get(TweenComponent.class).mode).isEqualTo(TweenMode.SINE_IN_OUT);
    }
}

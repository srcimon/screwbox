package dev.screwbox.core.particles;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Time;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.light.ShadowCasterComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.FixedRotationComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.environment.tweening.TweenComponent;
import dev.screwbox.core.environment.tweening.TweenOpacityComponent;
import dev.screwbox.core.environment.tweening.TweenScaleComponent;
import dev.screwbox.core.environment.tweening.TweenSpinComponent;
import dev.screwbox.core.graphics.ShaderBundle;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.SpriteBundle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.offset;

class ParticleOptionsTest {

    private ParticleOptions options;

    @BeforeEach
    void setUp() {
        options = ParticleOptions.unknownSource();
    }

    @Test
    void modifierIds_noModifiers_isEmpty() {
        assertThat(options.modifierIds()).isEmpty();
    }

    @Test
    void modifierIds_someModifiers_containsIds() {
        var result = options
                .animateOpacity()
                .customize("my-identifier", entity -> entity.add(new TweenScaleComponent(10, 20)));

        assertThat(result.modifierIds()).containsExactly("default-render-opacity", "my-identifier");
    }

    @Test
    void modifiers_multipleModifiersOfSameTypeAdded_onlyContainsLastModifier() {
        var particle = options
                .sprite(SpriteBundle.DOT_RED)
                .sprite(SpriteBundle.DOT_RED.get())
                .sprite(SpriteBundle.SLIME_MOVING)
                .sprites(SpriteBundle.DOT_BLUE)
                .sprites(SpriteBundle.DOT_BLUE.get());

        assertThat(particle.modifiers()).hasSize(1);
    }

    @Test
    void sprites_listOfSprites_setsSpritesToUse() {
        List<Sprite> sprites = List.of(SpriteBundle.DOT_BLUE.get(), SpriteBundle.DOT_YELLOW.get());

        Entity particle = applyOptionsOnTemplateParticle(options.sprites(sprites));

        assertThat(sprites.stream().map(Sprite::singleFrame).toList())
                .contains(particle.get(RenderComponent.class).sprite.singleFrame());
    }

    @Test
    void customize_addTooManyModifiers_throwsException() {
        var result = options;
        for (int i = 0; i < 100; i++) {
            result = result.customize("modifier-nr-" + i, entity -> entity.name("some name"));
        }
        final var noMoreCustomizable = result;
        assertThatThrownBy(() -> noMoreCustomizable.customize("modifier-nr-", entity -> entity.name("some name")))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("added more than 100 modifiers");
    }

    @Test
    void lifetimeSeconds_setsEaseDuration() {
        Entity particle = applyOptionsOnTemplateParticle(options.lifetimeSeconds(4));

        assertThat(particle.get(TweenComponent.class).duration).isEqualTo(Duration.ofSeconds(4));
    }

    @Test
    void randomLifeTimeSeconds_validRange_setsEaseDurationOnRandomValueInRange() {
        Entity particle = applyOptionsOnTemplateParticle(options.randomLifeTimeSeconds(4, 20));

        assertThat(particle.get(TweenComponent.class).duration.seconds()).isBetween(4L, 20L);
    }

    @Test
    void animateOpacity_addsTweenOpacityComponent() {
        Entity particle = applyOptionsOnTemplateParticle(options.animateOpacity());

        assertThat(particle.hasComponent(TweenOpacityComponent.class)).isTrue();
        assertThat(particle.get(TweenOpacityComponent.class).from).isEqualTo(Percent.zero());
        assertThat(particle.get(TweenOpacityComponent.class).to).isEqualTo(Percent.max());
    }

    @Test
    void baseSpeed_setsVelocity() {
        Entity particle = applyOptionsOnTemplateParticle(options.baseSpeed($(20, 0)));

        assertThat(particle.get(PhysicsComponent.class).velocity).isEqualTo($(20, 0));
    }

    @Test
    void randomBaseSpeed_strictValue_setsVelocity() {
        Entity particle = applyOptionsOnTemplateParticle(options.randomBaseSpeed(40));

        assertThat(particle.get(PhysicsComponent.class).velocity.length()).isEqualTo(40, offset(0.1));
    }

    @Test
    void randomBaseSpeed_range_setsVelocity() {
        Entity particle = applyOptionsOnTemplateParticle(options.randomBaseSpeed(40, 80));

        assertThat(particle.get(PhysicsComponent.class).velocity.length()).isBetween(40.0, 80.0);
    }

    @Test
    void castShadow_addsShadowCasterComponent() {
        Entity particle = applyOptionsOnTemplateParticle(options.castShadow());

        assertThat(particle.hasComponent(ShadowCasterComponent.class)).isTrue();
        assertThat(particle.get(ShadowCasterComponent.class).selfShadow).isFalse();
    }

    @Test
    void rotation_addsContinuousRotationComponent() {
        Entity particle = applyOptionsOnTemplateParticle(options.randomRotation(4));

        assertThat(particle.hasComponent(FixedRotationComponent.class)).isTrue();
        assertThat(particle.get(FixedRotationComponent.class).clockwiseRotationsPerSecond).isEqualTo(4);
    }

    @Test
    void randomRotation_addsContinuousRotationComponent() {
        Entity particle = applyOptionsOnTemplateParticle(options.randomRotation(-2, 4));

        assertThat(particle.hasComponent(FixedRotationComponent.class)).isTrue();
        assertThat(particle.get(FixedRotationComponent.class).clockwiseRotationsPerSecond).isBetween(-2.0, 4.0);
    }

    @Test
    void animateHorizontalSpin_addsSpinComponent() {
        Entity particle = applyOptionsOnTemplateParticle(options.animateHorizontalSpin());

        assertThat(particle.hasComponent(TweenSpinComponent.class)).isTrue();
        assertThat(particle.get(TweenSpinComponent.class).isSpinHorizontal).isTrue();
    }

    @Test
    void animateVerticalSpin_addsSpinComponent() {
        Entity particle = applyOptionsOnTemplateParticle(options.animateVerticalSpin());

        assertThat(particle.hasComponent(TweenSpinComponent.class)).isTrue();
        assertThat(particle.get(TweenSpinComponent.class).isSpinHorizontal).isFalse();
    }

    @Test
    void shaderSetup_addsShader() {
        Entity particle = applyOptionsOnTemplateParticle(options.shaderSetup(ShaderBundle.WATER));

        assertThat(particle.hasComponent(RenderComponent.class)).isTrue();
        assertThat(particle.get(RenderComponent.class).options.shaderSetup()).isEqualTo(ShaderBundle.WATER.get());
    }

    @Test
    void relativeDrawOrder_newOrder_updatesOrder() {
        options.relativeDrawOrder(10);

        assertThat(options.relativeDrawOrder()).isEqualTo(10);
    }

    @Test
    void drawOrder_alreadyUsingRelativeDrawOrder_doesNotResetRelativeDrawOrder() {
        var alteredOptions = options.relativeDrawOrder(10).drawOrder(4);

        assertThat(alteredOptions.relativeDrawOrder()).isEqualTo(10);
    }

    @Test
    void randomShaderOffset_noShader_throwsException() {
        final var optionsMissingShader = options.randomShaderOffset();
        assertThatThrownBy(() -> applyOptionsOnTemplateParticle(optionsMissingShader))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("shader setup is null");
    }

    @Test
    void randomShaderOffset_alreadyHasShader_randomizesShaderOffset() {
        Entity particle = applyOptionsOnTemplateParticle(options.shaderSetup(ShaderBundle.PIXELATE).randomShaderOffset());
        Time offset = particle.get(RenderComponent.class).options.shaderSetup().offset();

        Entity particle2 = applyOptionsOnTemplateParticle(options.shaderSetup(ShaderBundle.PIXELATE).randomShaderOffset());
        Time offset2 = particle2.get(RenderComponent.class).options.shaderSetup().offset();

        assertThat(offset).isNotEqualTo(offset2);
    }

    private Entity applyOptionsOnTemplateParticle(final ParticleOptions options) {
        final Entity particle = templateParticle();
        options.modifiers().forEach(modifier -> modifier.accept(particle));
        return particle;
    }

    private Entity templateParticle() {
        return new Entity()
                .add(new RenderComponent())
                .add(new TweenComponent(Duration.ofSeconds(100)))
                .add(new PhysicsComponent());
    }

}

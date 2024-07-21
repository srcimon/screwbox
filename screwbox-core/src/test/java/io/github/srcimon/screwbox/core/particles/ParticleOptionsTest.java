package io.github.srcimon.screwbox.core.particles;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.light.ShadowCasterComponent;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenOpacityComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenScaleComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.github.srcimon.screwbox.core.Vector.$;
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
    void baseSpeed_setsMomentum() {
        Entity particle = applyOptionsOnTemplateParticle(options.baseSpeed($(20, 0)));

        assertThat(particle.get(PhysicsComponent.class).momentum).isEqualTo($(20, 0));
    }

    @Test
    void randomBaseSpeed_stricktValue_setsMomentum() {
        Entity particle = applyOptionsOnTemplateParticle(options.randomBaseSpeed(40));

        assertThat(particle.get(PhysicsComponent.class).momentum.length()).isEqualTo(40, offset(0.1));
    }

    @Test
    void randomBaseSpeed_range_setsMomentum() {
        Entity particle = applyOptionsOnTemplateParticle(options.randomBaseSpeed(40, 80));

        assertThat(particle.get(PhysicsComponent.class).momentum.length()).isBetween(40.0, 80.0);
    }

    @Test
    void castShadow_addsShadowCasterComponent() {
        Entity particle = applyOptionsOnTemplateParticle(options.castShadow());

        assertThat(particle.hasComponent(ShadowCasterComponent.class)).isTrue();
        assertThat(particle.get(ShadowCasterComponent.class).selfShadow).isFalse();
    }

    private Entity applyOptionsOnTemplateParticle(ParticleOptions result) {
        Entity particle = templateParticle();
        return applyOptionsOnTemplateParticle(result, particle);
    }

    private Entity templateParticle() {
        return new Entity()
                .add(new RenderComponent())
                .add(new TweenComponent(Duration.ofSeconds(100)))
                .add(new PhysicsComponent());
    }

    private Entity applyOptionsOnTemplateParticle(ParticleOptions result, Entity particle) {
        result.modifiers().forEach(modifier -> modifier.accept(particle));
        return particle;
    }
}

package io.github.srcimon.screwbox.core.particles;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.assets.SpritesBundle;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.tweening.TweenComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenOpacityComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenScaleComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ParticleOptionsTest {

    private ParticleOptions options;

    @BeforeEach
    void setUp() {
        options = new ParticleOptions();
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
                .sprite(SpritesBundle.MOON_SURFACE_16)
                .sprite(SpritesBundle.MOON_SURFACE_16.get())
                .sprite(SpritesBundle.BLOB_ANIMATED_16)
                .sprites(SpritesBundle.DOT_BLUE_16)
                .sprites(SpritesBundle.DOT_BLUE_16.get());

        assertThat(particle.modifiers()).hasSize(1);
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
    void lifetimeSeconds_setsTweenModeDuration() {
        Entity particle = applyOptionsOnTemplateParticle(options.lifetimeSeconds(4));

        assertThat(particle.get(TweenComponent.class).duration).isEqualTo(Duration.ofSeconds(4));
    }

    @Test
    void randomLifeTimeSeconds_validRange_setsTweenModeDurationOnRandomValueInRange() {
        Entity particle = applyOptionsOnTemplateParticle(options.randomLifeTimeSeconds(4, 20));

        assertThat(particle.get(TweenComponent.class).duration.seconds()).isBetween(4l, 20l);
    }

    @Test
    void animateOpacity_addsTweenOpacityComponent() {
        Entity particle = applyOptionsOnTemplateParticle(options.animateOpacity());

        assertThat(particle.hasComponent(TweenOpacityComponent.class)).isTrue();
        assertThat(particle.get(TweenOpacityComponent.class).from).isEqualTo(Percent.zero());
        assertThat(particle.get(TweenOpacityComponent.class).to).isEqualTo(Percent.max());
    }

    private Entity applyOptionsOnTemplateParticle(ParticleOptions result) {
        Entity particle = new Entity().add(new TweenComponent(Duration.ofSeconds(100)));
        result.modifiers().forEach(modifier -> modifier.accept(particle));
        return particle;
    }
}

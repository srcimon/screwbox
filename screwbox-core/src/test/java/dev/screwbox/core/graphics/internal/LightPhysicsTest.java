package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Bounds;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoSettings;

import static dev.screwbox.core.Bounds.$$;
import static dev.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@MockitoSettings
class LightPhysicsTest {

    @InjectMocks
    LightPhysics lightPhysics;

    @Test
    void addShadowCaster_shadowCasterNull_throwsException() {
        assertThatThrownBy(() -> lightPhysics.addShadowCaster(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("shadowCaster must not be null");
    }

    @Test
    void addNoSelfShadowShadowCasters_shadowCasterNull_throwsException() {
        assertThatThrownBy(() -> lightPhysics.addNoSelfShadowShadowCasters(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("shadowCaster must not be null");
    }

    @Test
    void isCoveredByShadowCasters_noShadowCastersPresent_isFalse() {
        assertThat(lightPhysics.isCoveredByShadowCasters($(30, 10))).isFalse();
    }

    @Test
    void isCoveredByShadowCasters_noSelfshadowCasterObscuresPosition_isTrue() {
        lightPhysics.addNoSelfShadowShadowCasters($$(0, 5, 100, 40));
        assertThat(lightPhysics.isCoveredByShadowCasters($(30, 10))).isTrue();
    }

    @Test
    void isCoveredByShadowCasters_shadowCasterObscuresPosition_isTrue() {
        lightPhysics.addShadowCaster($$(0, 5, 100, 40));
        assertThat(lightPhysics.isCoveredByShadowCasters($(30, 10))).isTrue();
    }

    @Test
    void calculateArea_minAngleNotZero_containsLightboxPosition() {
        Bounds lightBox = $$(0, 5, 100, 40);
        var area = lightPhysics.calculateArea(lightBox, 0, 10);

        assertThat(area)
                .hasSize(11)
                .contains(lightBox.position());
    }

    @Test
    void calculateArea_noSelfShadowShadowCasterPresent_shadowCasterIsPartOfLight() {
        Bounds lightBox = $$(0, 0, 100, 100);
        Bounds shadowCaster = $$(0, 10, 100, 2);

        lightPhysics.addNoSelfShadowShadowCasters(shadowCaster);

        var area = lightPhysics.calculateArea(lightBox, 0, 360);

        assertThat(area)
                .isNotEmpty()
                .allMatch(point -> point.y() >= 10.0);
    }

    @Test
    void clear_removesAllExistingShadowCasters() {
        lightPhysics.addShadowCaster($$(0, 0, 2, 2));
        assertThat(lightPhysics.isCoveredByShadowCasters($(1, 1))).isTrue();

        lightPhysics.clear();

        assertThat(lightPhysics.isCoveredByShadowCasters($(1, 1))).isFalse();
    }

    @Test
    void isCoveredByShadowCasters_noShadowCasters_isFalse() {
        assertThat(lightPhysics.isCoveredByShadowCasters($$(1, 1, 40, 90))).isFalse();
    }

    @Test
    void isCoveredByShadowCasters_shadowCasterDoesNotIntersect_isFalse() {
        lightPhysics.addShadowCaster($$(50, 40, 200, 200));
        lightPhysics.addNoSelfShadowShadowCasters($$(50, 40, 200, 200));

        assertThat(lightPhysics.isCoveredByShadowCasters($$(1, 1, 40, 90))).isFalse();
    }

    @Test
    void isCoveredByShadowCasters_shadowCasterDoesNotFullyCoverArea_isFalse() {
        lightPhysics.addShadowCaster($$(20, 30, 200, 200));
        lightPhysics.addNoSelfShadowShadowCasters($$(20, 30, 200, 200));

        assertThat(lightPhysics.isCoveredByShadowCasters($$(1, 1, 40, 90))).isFalse();
    }

    @Test
    void isCoveredByShadowCasters_shadowCasterFullyCoverArea_isTrue() {
        lightPhysics.addShadowCaster($$(0, 0, 200, 200));

        assertThat(lightPhysics.isCoveredByShadowCasters($$(1, 1, 40, 90))).isTrue();
    }

    @Test
    void isCoveredByShadowCasters_noSelfShadowShadowCasterFullyCoverArea_isTrue() {
        lightPhysics.addNoSelfShadowShadowCasters($$(0, 0, 200, 200));

        assertThat(lightPhysics.isCoveredByShadowCasters($$(1, 1, 40, 90))).isTrue();
    }
}

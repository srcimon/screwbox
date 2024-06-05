package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Bounds;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.github.srcimon.screwbox.core.Bounds.$$;
import static io.github.srcimon.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LightPhysicsTest {

    LightPhysics lightPhysics;

    @BeforeEach
    void setUp() {
        lightPhysics = new LightPhysics();
    }

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
}

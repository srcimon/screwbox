package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Line;
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
    void addOccluder_affectedByShadowOccluderNull_throwsException() {
        assertThatThrownBy(() -> lightPhysics.addAffectedByShadowOccluder(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("occluder must not be null");
    }

    @Test
    void addOccluder_occluderNull_throwsException() {
        assertThatThrownBy(() -> lightPhysics.addOccluder(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("occluder must not be null");
    }

    @Test
    void isCoveredOccluders_noOccluderPresent_isFalse() {
        assertThat(lightPhysics.isOccluded($(30, 10))).isFalse();
    }

    @Test
    void addNoSelfOccluder_OccludersCoversPosition_isTrue() {
        lightPhysics.addOccluder($$(0, 5, 100, 40));
        assertThat(lightPhysics.isOccluded($(30, 10))).isTrue();
    }

    @Test
    void isOccluded_occludersCoverPosition_isTrue() {
        lightPhysics.addAffectedByShadowOccluder($$(0, 5, 100, 40));
        assertThat(lightPhysics.isOccluded($(30, 10))).isTrue();
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
    void isOccluded_noOccluders_isFalse() {
        assertThat(lightPhysics.isOccluded($(1, 1))).isFalse();
    }

    @Test
    void isOccluded_occludersDoesNotIntersect_isFalse() {
        lightPhysics.addAffectedByShadowOccluder($$(50, 40, 200, 200));
        lightPhysics.addOccluder($$(50, 40, 200, 200));

        assertThat(lightPhysics.isOccluded($(1, 1))).isFalse();
    }

    @Test
    void isOccluded_occluderDoesNotFullyCoverArea_isFalse() {
        lightPhysics.addAffectedByShadowOccluder($$(20, 30, 200, 200));
        lightPhysics.addOccluder($$(20, 30, 200, 200));

        assertThat(lightPhysics.isOccluded($(1, 1))).isFalse();
    }

    @Test
    void isOccluded_occluderFullyCoverArea_isTrue() {
        lightPhysics.addAffectedByShadowOccluder($$(0, 0, 200, 200));

        assertThat(lightPhysics.isOccluded($(1, 1))).isTrue();
    }

    @Test
    void isOccluded_noSelfOccluderFullyCoverArea_isTrue() {
        lightPhysics.addOccluder($$(0, 0, 200, 200));

        assertThat(lightPhysics.isOccluded($(1, 1))).isTrue();
    }

    @Test
    void isOccluded_noOccluderPresent_isFalse() {
        assertThat(lightPhysics.isOccluded(Line.between($(40, 10), $(90, 40)))).isFalse();
    }

    @Test
    void isOccluded_occludersDoNotIntersectLine_isFalse() {
        lightPhysics.addAffectedByShadowOccluder($$(50, 40, 200, 200));
        lightPhysics.addOccluder($$(150, 40, 200, 200));

        assertThat(lightPhysics.isOccluded(Line.between($(540, 10), $(990, 40)))).isFalse();
    }

    @Test
    void isOccluded_occluderIntersectLine_isTrue() {
        lightPhysics.addAffectedByShadowOccluder($$(50, -40, 200, 200));

        assertThat(lightPhysics.isOccluded(Line.between($(0, 10), $(990, 40)))).isTrue();
    }

    @Test
    void isOccluded_noSelfOccluderIntersectLine_isTrue() {
        lightPhysics.addOccluder($$(50, -40, 200, 200));

        assertThat(lightPhysics.isOccluded(Line.between($(0, 10), $(990, 40)))).isTrue();
    }
}

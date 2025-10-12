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
    void addOccluder_occluderNull_throwsException() {
        assertThatThrownBy(() -> lightPhysics.addOccluder(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("occluder must not be null");
    }

    @Test
    void addNoSelfOccluder_occluderNull_throwsException() {
        assertThatThrownBy(() -> lightPhysics.addNoSelfOccluder(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("occluder must not be null");
    }

    @Test
    void isCoveredOccluders_noOccluderPresent_isFalse() {
        assertThat(lightPhysics.isCoveredByOccluders($(30, 10))).isFalse();
    }

    @Test
    void addNoSelfOccluder_noSelfOccludersCoversPosition_isTrue() {
        lightPhysics.addNoSelfOccluder($$(0, 5, 100, 40));
        assertThat(lightPhysics.isCoveredByOccluders($(30, 10))).isTrue();
    }

    @Test
    void isCoveredByOccluders_occludersCoverPosition_isTrue() {
        lightPhysics.addOccluder($$(0, 5, 100, 40));
        assertThat(lightPhysics.isCoveredByOccluders($(30, 10))).isTrue();
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
    void calculateArea_noSelfSOccluderPresent_occluderIsPartOfLight() {
        Bounds lightBox = $$(0, 0, 100, 100);
        Bounds occluder = $$(0, 10, 100, 2);

        lightPhysics.addNoSelfOccluder(occluder);

        var area = lightPhysics.calculateArea(lightBox, 0, 360);

        assertThat(area)
                .isNotEmpty()
                .allMatch(point -> point.y() >= 10.0);
    }

    @Test
    void clear_removesAllExistingOccluders() {
        lightPhysics.addOccluder($$(0, 0, 2, 2));
        assertThat(lightPhysics.isCoveredByOccluders($(1, 1))).isTrue();

        lightPhysics.clear();

        assertThat(lightPhysics.isCoveredByOccluders($(1, 1))).isFalse();
    }

    @Test
    void isCoveredByOccluders_noOccluders_isFalse() {
        assertThat(lightPhysics.isCoveredByOccluders($$(1, 1, 40, 90))).isFalse();
    }

    @Test
    void isCoveredByOccluders_occludersDoesNotIntersect_isFalse() {
        lightPhysics.addOccluder($$(50, 40, 200, 200));
        lightPhysics.addNoSelfOccluder($$(50, 40, 200, 200));

        assertThat(lightPhysics.isCoveredByOccluders($$(1, 1, 40, 90))).isFalse();
    }

    @Test
    void isCoveredByOccluders_occluderDoesNotFullyCoverArea_isFalse() {
        lightPhysics.addOccluder($$(20, 30, 200, 200));
        lightPhysics.addNoSelfOccluder($$(20, 30, 200, 200));

        assertThat(lightPhysics.isCoveredByOccluders($$(1, 1, 40, 90))).isFalse();
    }

    @Test
    void isCoveredByOccluders_occluderFullyCoverArea_isTrue() {
        lightPhysics.addOccluder($$(0, 0, 200, 200));

        assertThat(lightPhysics.isCoveredByOccluders($$(1, 1, 40, 90))).isTrue();
    }

    @Test
    void isCoveredByOccluders_noSelfOccluderFullyCoverArea_isTrue() {
        lightPhysics.addNoSelfOccluder($$(0, 0, 200, 200));

        assertThat(lightPhysics.isCoveredByOccluders($$(1, 1, 40, 90))).isTrue();
    }
}

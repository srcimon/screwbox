package dev.screwbox.core.graphics;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LensFlareTest {

    @Test
    void newInstance_noRays_hasNoRays() {
        var lensFlare = LensFlare.noRays()
                .orb(2, 2, 0.5)
                .orb(1, 3, 0.5);

        assertThat(lensFlare.rayCount()).isZero();
        assertThat(lensFlare.orbs()).containsExactly(
                new LensFlare.Orb(2, 2, 0.5),
                new LensFlare.Orb(1, 3, 0.5));
    }
}

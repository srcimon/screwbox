package de.suzufa.screwbox.core.graphics;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AspectRatioTest {

    @Test
    void matches_dimensionMatchesAspectRatio_true() {
        Dimension resolution = Dimension.of(2048, 1536);

        boolean matches = AspectRatio.STANDARD.matches(resolution);

        assertThat(matches).isTrue();
    }

    @Test
    void matches_dimensionDoesntMatcheAspectRatio_flase() {
        Dimension resolution = Dimension.of(640, 480);

        boolean matches = AspectRatio.WIDESCREEN.matches(resolution);

        assertThat(matches).isFalse();
    }
}

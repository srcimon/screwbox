package io.github.srcimon.screwbox.core.graphics;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AspectRatioTest {

    @Test
    void matches_dimensionMatchesAspectRatio_true() {
        Size resolution = Size.of(2048, 1536);

        boolean matches = AspectRatio.STANDARD.matches(resolution);

        assertThat(matches).isTrue();
    }

    @Test
    void matches_dimensionDoesntMatcheAspectRatio_flase() {
        Size resolution = Size.of(640, 480);

        boolean matches = AspectRatio.WIDESCREEN.matches(resolution);

        assertThat(matches).isFalse();
    }
}

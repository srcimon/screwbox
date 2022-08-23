package de.suzufa.screwbox.core.loop.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.suzufa.screwbox.core.Duration;

class DefaultMetricsTest {

    private DefaultMetrics metrics;

    @BeforeEach
    void beforeEach() {
        metrics = new DefaultMetrics();
    }

    @Test
    void trackUpdateCycle_updatesMetrics() {
        Duration aSecond = Duration.ofNanos(1000);

        metrics.trackUpdateCycle(aSecond);

        assertThat(metrics.frameNumber()).isEqualTo(1);
        assertThat(metrics.framesPerSecond()).isPositive();
        assertThat(metrics.updateFactor()).isPositive();
        assertThat(metrics.updateFactor() * metrics.framesPerSecond()).isEqualTo(1.0, within(0.2));

        metrics.trackUpdateCycle(aSecond);
        metrics.trackUpdateCycle(aSecond);

        assertThat(metrics.frameNumber()).isEqualTo(3);
    }

}

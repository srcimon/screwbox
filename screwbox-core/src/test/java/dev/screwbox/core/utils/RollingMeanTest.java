package dev.screwbox.core.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RollingMeanTest {

    @Test
    void average_notFullyFilled_returnsAverageOfRecordedValues() {
        var rollingMean = new RollingMean(4);
        rollingMean.record(30.0);

        assertThat(rollingMean.average()).isEqualTo(30.0);

        rollingMean.record(20.0);

        assertThat(rollingMean.average()).isEqualTo(25.0);
    }

    @Test
    void average_fullyFilled_returnsAverage() {
        var rollingMean = new RollingMean(4);
        rollingMean.record(50.0);
        rollingMean.record(50.0);
        rollingMean.record(0.0);
        rollingMean.record(100.0);

        assertThat(rollingMean.average()).isEqualTo(50.0);
    }

    @Test
    void average_oldValuesWereOverwritten_returnsAverage() {
        var rollingMean = new RollingMean(2);
        rollingMean.record(50.0);
        rollingMean.record(50.0);
        rollingMean.record(0.0);
        rollingMean.record(100.0);

        assertThat(rollingMean.average()).isEqualTo(50.0);
    }
}

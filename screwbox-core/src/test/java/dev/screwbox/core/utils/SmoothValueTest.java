package dev.screwbox.core.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SmoothValueTest {

    @Test
    void average_noValueRecorded_isZero() {
        var rollingMean = new SmoothValue(4);

        assertThat(rollingMean.average()).isZero();
    }

    @Test
    void average_notFullyFilled_returnsAverageOfRecordedValues() {
        var rollingMean = new SmoothValue(4);
        rollingMean.recordSample(30.0);

        assertThat(rollingMean.average()).isEqualTo(30.0);

        rollingMean.recordSample(20.0);

        assertThat(rollingMean.average()).isEqualTo(25.0);
    }

    @Test
    void average_fullyFilled_returnsAverage() {
        var rollingMean = new SmoothValue(4);
        rollingMean.recordSample(50.0);
        rollingMean.recordSample(0.0);
        rollingMean.recordSample(100.0);

        assertThat(rollingMean.average(50.0)).isEqualTo(50.0);
    }

    @Test
    void average_oldValuesWereOverwritten_returnsAverage() {
        var rollingMean = new SmoothValue(2);
        rollingMean.recordSample(50.0);
        rollingMean.recordSample(50.0);
        rollingMean.recordSample(0.0);
        rollingMean.recordSample(100.0);

        assertThat(rollingMean.average()).isEqualTo(50.0);
    }

    @Test
    void newInstance_sizeZero_throwsException() {
        assertThatThrownBy(() -> new SmoothValue(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("size must be positive (actual value: 0)");
    }
}

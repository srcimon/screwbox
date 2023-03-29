package io.github.simonbas.screwbox.core;

import io.github.simonbas.screwbox.core.loop.Loop;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WarmUpIndicatorTest {

    @Mock
    Loop loop;

    @InjectMocks
    WarmUpIndicator warmUpIndicator;

    @Test
    void isWarmedUp_fastEnoughForEnoughProbesInARow_isTrue() {
        when(loop.updateDuration()).thenReturn(Duration.ofNanos(10));
        when(loop.runningTime()).thenReturn(Duration.ofSeconds(2));
        for (int i = 0; i < 20; i++) {
            warmUpIndicator.isWarmedUp();
        }

        assertThat(warmUpIndicator.isWarmedUp()).isTrue();
    }

    @Test
    void isWarmedUp_notFastEngoughButTimedOut_isTrue() {
        when(loop.runningTime()).thenReturn(Duration.ofSeconds(20));

        assertThat(warmUpIndicator.isWarmedUp()).isTrue();
    }

    @Test
    void isWarmedUp_notFastEngoughAndNotTimedOut_isFalse() {
        when(loop.updateDuration()).thenReturn(Duration.ofMillis(10));
        when(loop.runningTime()).thenReturn(Duration.ofSeconds(5));

        assertThat(warmUpIndicator.isWarmedUp()).isFalse();
    }
}

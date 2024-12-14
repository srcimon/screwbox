package io.github.srcimon.screwbox.core;

import io.github.srcimon.screwbox.core.log.Log;
import io.github.srcimon.screwbox.core.loop.Loop;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings
class WarmUpIndicatorTest {

    @Mock
    Loop loop;

    @Mock
    Log log;

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

        verify(log).warn("warmup timed out, starting anyway");
    }

    @Test
    void isWarmedUp_notFastEngoughAndNotTimedOut_isFalse() {
        when(loop.updateDuration()).thenReturn(Duration.ofMillis(10));
        when(loop.runningTime()).thenReturn(Duration.ofSeconds(5));

        assertThat(warmUpIndicator.isWarmedUp()).isFalse();
    }
}

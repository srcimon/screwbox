package io.github.simonbas.screwbox.core.loop;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoopTest {

    @Spy
    Loop loop;

    @Test
    void delta_withFactor_returnsMultipliedDelta() {
        when(loop.delta()).thenReturn(0.5);

        double result = loop.delta(4);

        assertThat(result).isEqualTo(2);
    }
}

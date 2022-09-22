package de.suzufa.screwbox.core.loop;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

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

package dev.screwbox.core.graphics;

import dev.screwbox.core.Angle;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@MockitoSettings
class ScreenTest {

    @Spy
    Screen screen;

    @Test
    void absoluteRotation_screenIsRotatedAndShaking_returnsSumOfBoth() {
        when(screen.shake()).thenReturn(Angle.degrees(20));
        when(screen.rotation()).thenReturn(Angle.degrees(100));

        assertThat(screen.absoluteRotation()).isEqualTo(Angle.degrees(120));
    }
}

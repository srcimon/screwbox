package io.github.srcimon.screwbox.core.graphics.layouts;

import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HorizontalLayoutTest {

    private static final ScreenBounds SCREEN = new ScreenBounds(20, 10, 642, 482);

    HorizontalLayout horizontalLayout;

    @BeforeEach
    void setUp() {
        horizontalLayout = new HorizontalLayout();
    }

    @Test
    void calculateBounds_oneViewport_fillsWholeScreen() {
        var result = horizontalLayout.calculateBounds(0, 1, 0, SCREEN);

        assertThat(result).isEqualTo(SCREEN);
    }

    @Test
    void calculateBounds_threeViewports_allHaveSameSize() {
        var firstViewport = horizontalLayout.calculateBounds(0, 3, 0,SCREEN);
        var secondViewport = horizontalLayout.calculateBounds(1, 3, 0,SCREEN);
        var thirdViewport = horizontalLayout.calculateBounds(2, 3, 0,SCREEN);

        assertThat(firstViewport.size())
                .isEqualTo(secondViewport.size())
                .isEqualTo(thirdViewport.size())
                .isEqualTo(Size.of(214, 482));

        assertThat(firstViewport.offset()).isEqualTo(Offset.at(20, 10));
        assertThat(secondViewport.offset()).isEqualTo(Offset.at(234, 10));
        assertThat(thirdViewport.offset()).isEqualTo(Offset.at(448, 10));
    }
}

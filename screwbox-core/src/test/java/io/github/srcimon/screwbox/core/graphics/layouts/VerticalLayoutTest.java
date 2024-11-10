package io.github.srcimon.screwbox.core.graphics.layouts;

import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VerticalLayoutTest {

    private static final ScreenBounds SCREEN = new ScreenBounds(20, 10, 640, 480);

    VerticalLayout verticalLayout;

    @BeforeEach
    void setUp() {
        verticalLayout = new VerticalLayout();
    }

    @Test
    void calculateBounds_oneViewport_fillsWholeScreen() {
        var result = verticalLayout.calculateBounds(0, 1, 0, SCREEN);

        assertThat(result).isEqualTo(SCREEN);
    }

    @Test
    void calculateBounds_threeViewports_allHaveSameSize() {
        var firstViewport = verticalLayout.calculateBounds(0, 3, 0, SCREEN);
        var secondViewport = verticalLayout.calculateBounds(1, 3, 0, SCREEN);
        var thirdViewport = verticalLayout.calculateBounds(2, 3, 0, SCREEN);

        assertThat(firstViewport.size())
                .isEqualTo(secondViewport.size())
                .isEqualTo(thirdViewport.size())
                .isEqualTo(Size.of(640, 160));

        assertThat(firstViewport.offset()).isEqualTo(Offset.at(20, 10));
        assertThat(secondViewport.offset()).isEqualTo(Offset.at(20, 170));
        assertThat(thirdViewport.offset()).isEqualTo(Offset.at(20, 330));
    }
}

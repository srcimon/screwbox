package io.github.srcimon.screwbox.core.ui.presets;

import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.ui.UiMenu;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.assertj.core.api.Assertions.assertThat;

@MockitoSettings
class SimpleUiLayouterTest {

    @InjectMocks
    SimpleUiLayouter layouter;

    @Test
    void layout_onBoundsWithOffset_layoutsWithOffset() {
        UiMenu menu = new UiMenu();

        var bounds = layouter.layout(menu.addItem("testitem"), menu, new ScreenBounds(40, 60, 640, 480));
        assertThat(bounds).isEqualTo(new ScreenBounds(40, 335, 640, 50));
    }
}

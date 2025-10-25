package dev.screwbox.core.ui.presets;

import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.ui.UiMenu;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.assertj.core.api.Assertions.assertThat;

@MockitoSettings
class SimpleUiLayoutTest {

    @InjectMocks
    SimpleUiLayout layout;

    @Test
    void layout_onBoundsWithOffset_layoutsWithOffset() {
        UiMenu menu = new UiMenu();

        var bounds = layout.layout(menu.addItem("test-item"), menu, new ScreenBounds(40, 60, 640, 480));
        assertThat(bounds).isEqualTo(new ScreenBounds(40, 335, 640, 50));
    }
}

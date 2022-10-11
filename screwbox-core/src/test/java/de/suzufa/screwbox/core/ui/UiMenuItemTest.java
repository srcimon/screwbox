package de.suzufa.screwbox.core.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.Engine;

@ExtendWith(MockitoExtension.class)
class UiMenuItemTest {

    @Mock
    Engine engine;

    @Test
    void label_withDynamicLabel_returnsDynamicValue() {
        when(engine.name()).thenReturn("Demo engine");
        var menuItem = new UiMenuItem(engine -> engine.name()) {

            @Override
            public void onActivate(Engine engine) {
                // TODO Auto-generated method stub

            }
        };
        assertThat(menuItem.label(engine)).isEqualTo("Demo engine");
    }

    @Test
    void label_withStaticLabel_returnsStaticValue() {
        var menuItem = new UiMenuItem("Static Label") {

            @Override
            public void onActivate(Engine engine) {
                // TODO Auto-generated method stub

            }
        };
        assertThat(menuItem.label(engine)).isEqualTo("Static Label");
    }
}

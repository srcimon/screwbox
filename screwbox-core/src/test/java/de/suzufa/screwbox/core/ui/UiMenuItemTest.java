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
    void label_dynamicLabel_returnsDynamicValue() {
        when(engine.name()).thenReturn("dynamic label demo");
        var menuItem = new UiMenuItem(engine -> engine.name()) {

            @Override
            public void onActivate(Engine engine) {
            }
        };
        assertThat(menuItem.label(engine)).isEqualTo("dynamic label demo");
    }

    @Test
    void label_staticLabel_returnsStaticValue() {
        var menuItem = new UiMenuItem("Static Label") {

            @Override
            public void onActivate(Engine engine) {
            }
        };
        assertThat(menuItem.label(engine)).isEqualTo("Static Label");
    }

    @Test
    void isActive_noActiveCondition_true() {
        var menuItem = new UiMenuItem("Always active") {

            @Override
            public void onActivate(Engine engine) {
            }
        };
        assertThat(menuItem.isActive(engine)).isTrue();
    }

    @Test
    void isActive_activeConditionMet_true() {
        when(engine.name()).thenReturn("active");
        var menuItem = new UiMenuItem("Active on Engine named 'active'") {

            @Override
            public void onActivate(Engine engine) {
            }
        }.activeCondition(engine -> engine.name().equals("active"));

        assertThat(menuItem.isActive(engine)).isTrue();
    }

    @Test
    void isActive_activeConditionNotMet_false() {
        when(engine.name()).thenReturn("inactive");
        var menuItem = new UiMenuItem("Active on Engine named 'active'") {

            @Override
            public void onActivate(Engine engine) {
            }
        }.activeCondition(engine -> engine.name().equals("active"));

        assertThat(menuItem.isActive(engine)).isFalse();
    }
}

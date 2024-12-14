package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.Engine;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings
class UiMenuItemTest {

    @Mock
    Engine engine;

    @Test
    void label_dynamicLabel_returnsDynamicValue() {
        when(engine.name()).thenReturn("dynamic label demo");
        var menuItem = new UiMenuItem(Engine::name);
        assertThat(menuItem.label(engine)).isEqualTo("dynamic label demo");
    }

    @Test
    void label_staticLabel_returnsStaticValue() {
        var menuItem = new UiMenuItem("Static Label");
        assertThat(menuItem.label(engine)).isEqualTo("Static Label");
    }

    @Test
    void isActive_noActiveCondition_true() {
        var menuItem = new UiMenuItem("Always active");
        assertThat(menuItem.isActive(engine)).isTrue();
    }

    @Test
    void disabled_setsActiveConditionToNever() {
        var menuItem = new UiMenuItem("never active").disabled();

        assertThat(menuItem.isActive(engine)).isFalse();
    }

    @Test
    void isActive_activeConditionMet_true() {
        when(engine.name()).thenReturn("active");
        var menuItem = new UiMenuItem("Active on Engine named 'active'")
                .activeCondition(e -> e.name().equals("active"));

        assertThat(menuItem.isActive(engine)).isTrue();
    }

    @Test
    void isActive_activeConditionNotMet_false() {
        when(engine.name()).thenReturn("inactive");
        var menuItem = new UiMenuItem("Active on Engine named 'active'")
                .activeCondition(e -> e.name().equals("active"));

        assertThat(menuItem.isActive(engine)).isFalse();
    }

    @Test
    void trigger_noAction_noException() {
        var menuItem = new UiMenuItem("Active on Engine named 'active'");

        assertThatNoException().isThrownBy(() -> menuItem.trigger(engine));
    }

    @Test
    void trigger_withAction_triggersAction() {
        var menuItem = new UiMenuItem("Active on Engine named 'active'").onActivate(Engine::stop);

        menuItem.trigger(engine);

        verify(engine).stop();
    }
}

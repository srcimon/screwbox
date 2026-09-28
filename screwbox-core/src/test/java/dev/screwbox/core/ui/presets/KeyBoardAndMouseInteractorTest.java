package dev.screwbox.core.ui.presets;

import dev.screwbox.core.Engine;
import dev.screwbox.core.keyboard.Key;
import dev.screwbox.core.keyboard.Keyboard;
import dev.screwbox.core.mouse.Mouse;
import dev.screwbox.core.ui.Ui;
import dev.screwbox.core.ui.UiMenu;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings
class KeyBoardAndMouseInteractorTest {

    @Mock
    Engine engine;

    @Mock
    UiMenu menu;

    @Mock
    Ui ui;

    @Mock
    Keyboard keyboard;

    @Mock
    Mouse mouse;

    @InjectMocks
    KeyboardAndMouseInteractor interactor;

    @Test
    void interactWith_arrowDownPressed_selectsNextItem() {
        when(engine.ui()).thenReturn(ui);
        when(engine.mouse()).thenReturn(mouse);
        when(engine.keyboard()).thenReturn(keyboard);
        when(keyboard.isPressed(Key.ARROW_DOWN)).thenReturn(true);

        interactor.interactWith(menu, engine);

        verify(menu).nextItem(engine);
    }

    @Test
    void interactWith_arrowUpPressed_selectsPreviousItem() {
        when(engine.ui()).thenReturn(ui);
        when(engine.mouse()).thenReturn(mouse);
        when(engine.keyboard()).thenReturn(keyboard);
        when(keyboard.isPressed(Key.ARROW_DOWN)).thenReturn(false);
        when(keyboard.isPressed(Key.ARROW_UP)).thenReturn(true);

        interactor.interactWith(menu, engine);

        verify(menu).previousItem(engine);
    }
}

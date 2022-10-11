package de.suzufa.screwbox.core.ui.internal;

import static de.suzufa.screwbox.core.graphics.Dimension.of;
import static de.suzufa.screwbox.core.graphics.Offset.at;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.graphics.Graphics;
import de.suzufa.screwbox.core.graphics.Window;
import de.suzufa.screwbox.core.graphics.WindowBounds;
import de.suzufa.screwbox.core.ui.UiInteractor;
import de.suzufa.screwbox.core.ui.UiLayouter;
import de.suzufa.screwbox.core.ui.UiMenu;
import de.suzufa.screwbox.core.ui.UiMenuItem;
import de.suzufa.screwbox.core.ui.UiRenderer;

@ExtendWith(MockitoExtension.class)
class DefaultUiTest {

    @InjectMocks
    DefaultUi ui;

    @Mock
    Engine engine;

    @Mock
    UiInteractor interactor;

    @Mock
    UiLayouter layouter;

    @Mock
    UiRenderer renderer;

    @BeforeEach
    void beforeEach() {
        ui
                .setInteractor(interactor)
                .setLayouter(layouter)
                .setRenderer(renderer);
    }

    @Test
    void update_noMenu_noInteractionOrRendering() {
        ui.update();

        verify(interactor, never()).interactWith(any(), any(), any());
        verify(layouter, never()).calculateBounds(any(), any(), any());
        verify(renderer, never()).renderInactiveItem(any(), any(), any());
        verify(renderer, never()).renderSelectableItem(any(), any(), any());
        verify(renderer, never()).renderSelectedItem(any(), any(), any());
    }

    @Test
    void update_menuPresent_interactsAndRendersMenu() {
        Graphics graphics = mock(Graphics.class);
        Window window = mock(Window.class);
        when(graphics.window()).thenReturn(window);
        when(engine.graphics()).thenReturn(graphics);
        UiMenuItem item = new UiMenuItem("some button");
        UiMenu menu = new UiMenu().add(item);
        WindowBounds layoutBounds = new WindowBounds(at(20, 40), of(100, 20));
        when(window.isVisible(layoutBounds)).thenReturn(true);
        when(layouter.calculateBounds(item, menu, window)).thenReturn(layoutBounds);
        ui.openMenu(menu);

        ui.update();

        verify(interactor).interactWith(menu, layouter, engine);
        verify(renderer).renderSelectedItem("some button", layoutBounds, window);
    }
}

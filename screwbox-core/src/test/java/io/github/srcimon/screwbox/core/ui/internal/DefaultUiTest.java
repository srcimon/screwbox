package io.github.srcimon.screwbox.core.ui.internal;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.scenes.internal.DefaultScenes;
import io.github.srcimon.screwbox.core.ui.UiInteractor;
import io.github.srcimon.screwbox.core.ui.UiLayouter;
import io.github.srcimon.screwbox.core.ui.UiMenu;
import io.github.srcimon.screwbox.core.ui.UiMenuItem;
import io.github.srcimon.screwbox.core.ui.UiRenderer;
import io.github.srcimon.screwbox.core.utils.Latch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultUiTest {

    @InjectMocks
    DefaultUi ui;

    @Mock
    Engine engine;

    @Mock
    DefaultScenes scenes;

    @Mock
    UiInteractor interactor;

    @Mock
    UiLayouter layouter;

    @Mock
    UiRenderer renderer;

    @Mock
    Canvas canvas;

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
    void update_interactorDisablesCurrentSelection_switchesToNextMenuItem() {
        when(scenes.isShowingLoadingScene()).thenReturn(false);
        var activated = Latch.of(false, true);

        UiMenu menu = new UiMenu();
        UiMenuItem firstItem = menu.addItem("some button").activeCondition(e -> activated.active())
                .onActivate(e -> activated.toggle());
        menu.addItem("some button");
        ScreenBounds layoutBounds = new ScreenBounds(Offset.at(20, 40), Size.of(100, 20));
        when(canvas.isVisible(layoutBounds)).thenReturn(true);

        when(layouter.calculateBounds(firstItem, menu, null)).thenReturn(layoutBounds);
        ui.openMenu(menu);

        assertThat(menu.activeItemIndex()).isZero();

        ui.update();

        assertThat(menu.activeItemIndex()).isEqualTo(1);
    }

    @Test
    void update_menuPresent_interactsAndRendersMenu() {
        when(scenes.isShowingLoadingScene()).thenReturn(false);
        UiMenu menu = new UiMenu();
        UiMenuItem item = menu.addItem("some button");
        ScreenBounds layoutBounds = new ScreenBounds(Offset.at(20, 40), Size.of(100, 20));
        when(canvas.isVisible(layoutBounds)).thenReturn(true);
        when(layouter.calculateBounds(item, menu, null)).thenReturn(layoutBounds);
        ui.openMenu(menu);

        ui.update();

        verify(interactor).interactWith(menu, layouter, engine);
        verify(renderer).renderSelectedItem("some button", layoutBounds, canvas);
    }

    @Test
    void currentMenu_menuOpen_returnsMenu() {
        UiMenu menu = new UiMenu();

        ui.openMenu(menu);

        assertThat(ui.currentMenu()).hasValue(menu);
    }

    @Test
    void currentMenu_noOpenMenu_isEmpty() {
        assertThat(ui.currentMenu()).isEmpty();
    }

    @Test
    void openPreviousMenu_noPreviousMenu_throwsException() {
        assertThatThrownBy(() -> ui.openPreviousMenu())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("there is no previous menu to navigate back to");
    }

    @Test
    void openPreviousMenu_previousMenuPresent_switchesBackToPreviousMenu() {
        UiMenu optionsMenu = new UiMenu();
        ui.openMenu(optionsMenu);
        ui.openMenu(new UiMenu());

        ui.openPreviousMenu();

        assertThat(ui.currentMenu()).contains(optionsMenu);
    }

    @Test
    void openMenu_menuConsumerGiven_addsCustomizedMenu() {
        ui.openMenu(menu -> {
            menu.addItem("test1");
            menu.addItem("test2");
        });

        assertThat(ui.currentMenu()).isPresent();
        assertThat(ui.currentMenu().get().itemCount()).isEqualTo(2);
    }

}

package de.suzufa.screwbox.core.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UiMenuTest {

    private UiMenu menu;

    @BeforeEach
    void beforeEach() {
        menu = new UiMenu();
    }

    @Test
    void activeItem_noItems_throwsException() {
        assertThatThrownBy(() -> menu.activeItem())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("no menu item present");
    }

    @Test
    void activeItem_itemsPresent_returnsActiveItem() {
        UiMenuItem gameOptions = mock(UiMenuItem.class);
        UiMenuItem quitGame = mock(UiMenuItem.class);

        menu.add(gameOptions);
        menu.add(quitGame);

        assertThat(menu.activeItem()).isEqualTo(gameOptions);
    }

    @Test
    void nextItem_itemsPresent_switchesActiveItem() {
        UiMenuItem gameOptions = mock(UiMenuItem.class);
        UiMenuItem quitGame = mock(UiMenuItem.class);

        menu.add(gameOptions);
        menu.add(quitGame);

        menu.nextItem();

        assertThat(menu.activeItem()).isEqualTo(quitGame);
    }

    @Test
    void nextItem_atEndOfList_doenstSwitchItem() {
        UiMenuItem gameOptions = mock(UiMenuItem.class);
        UiMenuItem quitGame = mock(UiMenuItem.class);

        menu.add(gameOptions);
        menu.add(quitGame);

        menu.nextItem();
        menu.nextItem();
        menu.nextItem();

        assertThat(menu.activeItem()).isEqualTo(quitGame);
    }

    @Test
    void itemCount_returnsItemCount() {
        menu.add(mock(UiMenuItem.class));

        assertThat(menu.itemCount()).isEqualTo(1);
    }

}

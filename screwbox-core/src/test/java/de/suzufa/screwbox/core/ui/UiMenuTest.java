package de.suzufa.screwbox.core.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.Engine;

@ExtendWith(MockitoExtension.class)
class UiMenuTest {

    UiMenu menu;

    @Mock
    Engine engine;

    @BeforeEach
    void beforeEach() {
        menu = new UiMenu();
    }

    @Test
    void selectedItem_noItems_throwsException() {
        assertThatThrownBy(() -> menu.selectedItem())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("no menu item present");
    }

    @Test
    void selectedItem_itemsPresent_returnsSelectedItem() {
        UiMenuItem gameOptions = new UiMenuItem("Unnamed");
        UiMenuItem quitGame = new UiMenuItem("Unnamed");

        menu.add(gameOptions);
        menu.add(quitGame);

        assertThat(menu.selectedItem()).isEqualTo(gameOptions);
    }

    @Test
    void nextItem_itemsPresent_switchesActiveItem() {
        UiMenuItem gameOptions = new UiMenuItem("Unnamed");
        UiMenuItem quitGame = new UiMenuItem("Unnamed");

        menu.add(gameOptions);
        menu.add(quitGame);

        menu.nextItem(engine);

        assertThat(menu.selectedItem()).isEqualTo(quitGame);
    }

    @Test
    void nextItem_atEndOfList_doenstSwitchItem() {
        UiMenuItem gameOptions = new UiMenuItem("Unnamed");
        UiMenuItem quitGame = new UiMenuItem("Unnamed");

        menu.add(gameOptions);
        menu.add(quitGame);

        menu.nextItem(engine);
        menu.nextItem(engine);
        menu.nextItem(engine);

        assertThat(menu.selectedItem()).isEqualTo(quitGame);
    }

    @Test
    void itemCount_returnsItemCount() {
        menu.add(new UiMenuItem("Unnamed"));

        assertThat(menu.itemCount()).isEqualTo(1);
    }

    @Test
    void nextItem_noActiveItem_doenstSwitchItem() {
        UiMenuItem gameOptions = new UiMenuItem("Unnamed").activeCondition(e -> false);
        UiMenuItem quitGame = new UiMenuItem("Unnamed").activeCondition(e -> false);

        menu.add(gameOptions);
        menu.add(quitGame);

        menu.nextItem(engine);

        assertThat(menu.selectedItem()).isEqualTo(gameOptions);
    }

    @Test
    void previousItem_noActiveItem_doenstSwitchItem() {
        UiMenuItem gameOptions = new UiMenuItem("Unnamed").activeCondition(e -> false);
        UiMenuItem quitGame = new UiMenuItem("Unnamed").activeCondition(e -> false);

        menu.add(gameOptions);
        menu.add(quitGame);

        menu.previousItem(engine);

        assertThat(menu.selectedItem()).isEqualTo(gameOptions);
    }

}

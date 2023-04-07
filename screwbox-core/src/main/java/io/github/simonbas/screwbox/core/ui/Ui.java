package io.github.simonbas.screwbox.core.ui;

import java.util.Optional;

/**
 * Create simple ingame menus.
 */
public interface Ui {

    Ui openMenu(UiMenu menu);

    Ui openPreviousMenu();

    Ui closeMenu();

    Ui setRenderer(UiRenderer renderer);

    Ui setInteractor(UiInteractor interactor);

    Ui setLayouter(UiLayouter layouter);

    Optional<UiMenu> currentMenu();

}

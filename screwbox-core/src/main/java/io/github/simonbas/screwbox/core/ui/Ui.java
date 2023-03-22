package io.github.simonbas.screwbox.core.ui;

import java.util.Optional;

public interface Ui {

    Ui openMenu(UiMenu menu);

    Ui setRenderer(UiRenderer renderer);

    Ui setInteractor(UiInteractor interactor);

    Ui setLayouter(UiLayouter layouter);

    Ui closeMenu();

    Optional<UiMenu> currentMenu();
}

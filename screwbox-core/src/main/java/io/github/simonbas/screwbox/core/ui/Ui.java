package io.github.simonbas.screwbox.core.ui;

public interface Ui {

    Ui openMenu(UiMenu menu);

    Ui setRenderer(UiRenderer renderer);

    Ui setInteractor(UiInteractor interactor);

    Ui setLayouter(UiLayouter layouter);

    Ui closeMenu();
}

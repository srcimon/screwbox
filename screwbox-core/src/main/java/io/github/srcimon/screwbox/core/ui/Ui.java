package io.github.srcimon.screwbox.core.ui;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Create simple ingame menus.
 */
public interface Ui {

    /**
     * Opens a {@link UiMenu}.
     * 
     * @see #closeMenu() 
     */
    Ui openMenu(UiMenu menu);

    Ui openMenu(Consumer<UiMenu> menu);

    /**
     * Opens the previous {@link UiMenu}. Used to navigate back from sub menus.
     */
    Ui openPreviousMenu();

    /**
     * Closes the current open {@link UiMenu}.
     * 
     * @see #openMenu(UiMenu)
     */
    Ui closeMenu();

    /**
     * Returns the current {@link UiMenu}. Is {@link Optional#empty()} when there
     * currently is no open {@link UiMenu}.
     */
    Optional<UiMenu> currentMenu();

    Ui setRenderer(UiRenderer renderer);

    Ui setInteractor(UiInteractor interactor);

    Ui setLayouter(UiLayouter layouter);

}

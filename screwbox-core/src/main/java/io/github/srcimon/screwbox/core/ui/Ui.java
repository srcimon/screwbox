package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.environment.rendering.RenderUiSystem;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Create simple ingame menus.
 */
public interface Ui {

    Ui showNotification(Notification notification);

    Ui renderNotifications();

    /**
     * Opens a {@link UiMenu}. To actually see the menu you must call {@link #renderMenu()}. To make this easier just
     * call {@link Environment#enableRendering()}.
     *
     * @see #openMenu(Consumer)
     * @see #closeMenu()
     */
    Ui openMenu(UiMenu menu);

    /**
     * Opens a {@link UiMenu}. Allows inline definition of menu.
     *
     * @see #openMenu(UiMenu)
     * @see #closeMenu()
     */
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

    /**
     * Renders the currently open menu. Can be automated by using {@link RenderUiSystem}.
     *
     * @see RenderUiSystem
     * @see Environment#enableRendering()
     * @since 2.6.0
     */
    Ui renderMenu();

    Ui setRenderer(UiRenderer renderer);

    Ui setInteractor(UiInteractor interactor);

    Ui setLayouter(UiLayouter layouter);

}

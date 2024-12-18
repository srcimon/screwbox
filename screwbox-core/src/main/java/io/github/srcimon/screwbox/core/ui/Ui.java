package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.environment.rendering.RenderNotificationsSystem;
import io.github.srcimon.screwbox.core.environment.rendering.RenderUiSystem;
import io.github.srcimon.screwbox.core.graphics.Screen;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Create simple ingame menus and show {@link Notification notifications}.
 */
public interface Ui {

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

    /**
     * Sets the renderer for ui elements such as {@link UiMenu menus}.
     */
    Ui setRenderer(UiRenderer renderer);

    /**
     * Sets the interactor for ui elements such as {@link UiMenu menus}.
     */
    Ui setInteractor(UiInteractor interactor);

    /**
     * Sets the layouter for ui elements such as {@link UiMenu menus}.
     */
    Ui setLayouter(UiLayouter layouter);

    /**
     * Sets the renderer for {@link Notification notifications}.
     *
     * @since 2.8.0
     */
    Ui setNotificationRender(NotificationRenderer renderer);

    /**
     * Sets the layouter for {@link Notification notifications}.
     *
     * @since 2.8.0
     */
    Ui setNotificationLayouter(NotificationLayouter layouter);

    /**
     * Sets the timeout for {@link Notification notifications}. {@link Notification Notifications} will be removed
     * from screen when specified timeout is reached. Default is 8 seconds.
     *
     * @since 2.8.0
     */
    Ui setNotificationTimeout(Duration timeout);

    /**
     * Renders {@link Notification notifications} on the {@link Screen}. Can be automated using {@link RenderNotificationsSystem}.
     *
     * @see RenderNotificationsSystem
     * @see Environment#enableRendering()
     * @since 2.8.0
     */
    Ui renderNotifications();

    /**
     * Sets the default notification {@link Sound}. This sound will be played on {@link #showNotification(NotificationDetails)}
     * and {@link NotificationDetails} don't specify a custom sound.
     * Null value mutes audio playback on new {@link Notification notifications}.
     *
     * @since 2.8.0
     */
    Ui setNotificationSound(Supplier<Sound> sound);

    /**
     * Shows the specified notification. Uses {@link NotificationRenderer} for rendering.
     *
     * @since 2.8.0
     */
    Ui showNotification(NotificationDetails notification);

    /**
     * Returns a list of all current {@link Notification notifications} in order of creation.
     * {@link Notification Notifications} will be removed when timed out.
     *
     * @since 2.8.0
     */
    List<Notification> notifications();
}

package dev.screwbox.core.ui.internal;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Time;
import dev.screwbox.core.audio.Sound;
import dev.screwbox.core.audio.SoundBundle;
import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.loop.internal.Updatable;
import dev.screwbox.core.scenes.internal.DefaultScenes;
import dev.screwbox.core.ui.Notification;
import dev.screwbox.core.ui.NotificationDetails;
import dev.screwbox.core.ui.NotificationLayouter;
import dev.screwbox.core.ui.NotificationRenderer;
import dev.screwbox.core.ui.Ui;
import dev.screwbox.core.ui.UiInteractor;
import dev.screwbox.core.ui.UiLayouter;
import dev.screwbox.core.ui.UiMenu;
import dev.screwbox.core.ui.UiRenderer;
import dev.screwbox.core.ui.presets.KeyboardInteractor;
import dev.screwbox.core.ui.presets.SimpleUiLayouter;
import dev.screwbox.core.ui.presets.SimpleUiRenderer;
import dev.screwbox.core.ui.presets.SpinningIconNotificationRenderer;
import dev.screwbox.core.ui.presets.TopLeftNotificationLayouter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class DefaultUi implements Ui, Updatable {

    private final Engine engine;
    private final DefaultScenes scenes;
    private final Canvas canvas;

    private UiRenderer renderer = new SimpleUiRenderer();
    private UiInteractor interactor = new KeyboardInteractor();
    private UiLayouter layouter = new SimpleUiLayouter();
    private NotificationRenderer notificationRenderer = new SpinningIconNotificationRenderer();
    private NotificationLayouter notificationLayouter = new TopLeftNotificationLayouter();
    private Supplier<Sound> notificationSound = SoundBundle.NOTIFY;
    private OpenMenu openMenu = new OpenMenu(null, null);

    private record OpenMenu(UiMenu menu, OpenMenu previous) {
    }

    public DefaultUi(final Engine engine, final DefaultScenes scenes, final Canvas canvas) {
        this.engine = engine;
        this.scenes = scenes;
        this.canvas = canvas;
    }

    private final List<DefaultNotification> notifications = new ArrayList<>();
    private Duration notificationTimeout = Duration.ofSeconds(8);

    @Override
    public Ui showNotification(final NotificationDetails notification) {
        Objects.requireNonNull(notification, "notification must not be null");
        final Time now = engine.loop().time();
        notifications.add(new DefaultNotification(notification, now));
        notification.sound().ifPresentOrElse(sound -> engine.audio().playSound(sound),
                () -> Optional.ofNullable(notificationSound).ifPresent(defaultSound -> engine.audio().playSound(defaultSound)));
        return this;
    }

    @Override
    public Ui renderNotifications() {
        int index = 0;
        for (final var notification : notifications) {
            final var notificationBounds = notificationLayouter.layout(index, notification, canvas.bounds());
            notificationRenderer.render(notification, notificationBounds, canvas);
            index++;
        }
        return this;
    }

    @Override
    public List<Notification> notifications() {
        return Collections.unmodifiableList(notifications);
    }

    @Override
    public Ui openMenu(final UiMenu menu) {
        openMenu = new OpenMenu(menu, openMenu);
        return this;
    }

    @Override
    public Ui openMenu(final Consumer<UiMenu> menu) {
        UiMenu newMenu = new UiMenu();
        menu.accept(newMenu);
        openMenu(newMenu);
        return this;
    }

    @Override
    public Ui openPreviousMenu() {
        if (isNull(openMenu.previous)) {
            throw new IllegalStateException("there is no previous menu to navigate back to");
        }
        openMenu = openMenu.previous;
        return this;
    }

    @Override
    public void update() {
        final var menu = openMenu.menu;
        if (nonNull(menu) && !scenes.isShowingLoadingScene()) {
            interactor.interactWith(menu, layouter, engine);
            if (!menu.isActive(menu.selectedItem(), engine)) {
                menu.nextItem(engine);
            }
        }
        if (!notifications.isEmpty()) {
            for (final var notification : notifications) {
                notification.updateProgress(notificationTimeout.progress(notification.timeCreated(), engine.loop().time()));
            }
            notifications.removeIf(notification -> notification.progress().isMax());
        }
    }

    @Override
    public Ui setNotificationRender(final NotificationRenderer renderer) {
        notificationRenderer = Objects.requireNonNull(renderer, "renderer must not be null");
        return this;
    }

    @Override
    public Ui setNotificationLayouter(NotificationLayouter layouter) {
        notificationLayouter = Objects.requireNonNull(layouter, "layouter must not be null");
        return this;
    }

    @Override
    public Ui setNotificationTimeout(final Duration timeout) {
        notificationTimeout = Objects.requireNonNull(timeout, "timeout must not be null");
        return this;
    }

    @Override
    public Ui setNotificationSound(Supplier<Sound> sound) {
        notificationSound = sound;
        return this;
    }

    @Override
    public Ui setRenderer(final UiRenderer renderer) {
        this.renderer = renderer;
        return this;
    }

    @Override
    public Ui setInteractor(final UiInteractor interactor) {
        this.interactor = interactor;
        return this;
    }

    @Override
    public Ui closeMenu() {
        openMenu = new OpenMenu(null, openMenu);
        return this;
    }

    @Override
    public Optional<UiMenu> currentMenu() {
        return Optional.ofNullable(openMenu.menu);
    }

    @Override
    public Ui setLayouter(final UiLayouter layouter) {
        this.layouter = layouter;
        return this;
    }

    @Override
    public Ui renderMenu() {
        final var menu = openMenu.menu;
        if (isNull(menu)) {
            return this;
        }
        for (final var item : menu.items()) {
            final var bounds = layouter.layout(item, menu, canvas.bounds());
            if (canvas.isVisible(bounds)) {
                String label = item.label(engine);
                if (menu.isSelectedItem(item)) {
                    renderer.renderSelectedItem(label, bounds, canvas);
                } else if (menu.isActive(item, engine)) {
                    renderer.renderSelectableItem(label, bounds, canvas);
                } else {
                    renderer.renderInactiveItem(label, bounds, canvas);
                }
            }
        }
        return this;
    }
}

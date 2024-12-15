package io.github.srcimon.screwbox.core.ui.internal;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.assets.FontBundle;
import io.github.srcimon.screwbox.core.audio.SoundBundle;
import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.drawoptions.TextDrawOptions;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;
import io.github.srcimon.screwbox.core.scenes.internal.DefaultScenes;
import io.github.srcimon.screwbox.core.ui.KeyboardInteractor;
import io.github.srcimon.screwbox.core.ui.Notification;
import io.github.srcimon.screwbox.core.ui.SimpleUiLayouter;
import io.github.srcimon.screwbox.core.ui.SimpleUiRenderer;
import io.github.srcimon.screwbox.core.ui.Ui;
import io.github.srcimon.screwbox.core.ui.UiInteractor;
import io.github.srcimon.screwbox.core.ui.UiLayouter;
import io.github.srcimon.screwbox.core.ui.UiMenu;
import io.github.srcimon.screwbox.core.ui.UiRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class DefaultUi implements Ui, Updatable {

    private final Engine engine;
    private final DefaultScenes scenes;
    private final Canvas canvas;

    private UiRenderer renderer = new SimpleUiRenderer();
    private UiInteractor interactor = new KeyboardInteractor();
    private UiLayouter layouter = new SimpleUiLayouter();

    private OpenMenu openMenu = new OpenMenu(null, null);

    private record OpenMenu(UiMenu menu, OpenMenu previous) {
    }

    private record ActiveNotification(Notification notification, Time activationTime) {

    }

    public DefaultUi(final Engine engine, final DefaultScenes scenes, final Canvas canvas) {
        this.engine = engine;
        this.scenes = scenes;
        this.canvas = canvas;
    }

    private final List<ActiveNotification> activeNotifications = new ArrayList<>();
    private final Duration defaultNotificationTimeout = Duration.ofSeconds(4);//TODO change via method

    @Override
    public Ui showNotification(final Notification notification) {
        activeNotifications.add(new ActiveNotification(notification, engine.loop().lastUpdate()));
        engine.audio().playSound(SoundBundle.NOTIFY);
        return this;
    }

    @Override
    public Ui renderNotifications() {
        for (int i = 0; i < activeNotifications.size(); i++) {
            var progress = defaultNotificationTimeout.progress(activeNotifications.get(i).activationTime, engine.loop().lastUpdate());
            engine.graphics().canvas().drawText(
                    engine.graphics().canvas().center().addY((int) (progress.value() * 300.0)),
                    activeNotifications.get(i).notification.text(), TextDrawOptions.font(FontBundle.SKINNY_SANS)
                            .scale(Ease.IN_PLATEAU.applyOn(progress).value() * 3.0)
                            .alignCenter()
                            .opacity(Ease.SINE_IN_OUT.applyOn(progress)));
        }
        return this;
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
        if (!activeNotifications.isEmpty()) {
            for (final var notification : new ArrayList<>(activeNotifications)) {
                var progress = defaultNotificationTimeout.progress(notification.activationTime, engine.loop().lastUpdate());
                if (progress.isMax()) {
                    activeNotifications.remove(notification);
                }
            }
        }
    }

    @Override
    public Ui renderMenu() {
        final var menu = openMenu.menu;
        if (isNull(menu) || scenes.isShowingLoadingScene()) {
            return this;
        }
        for (final var item : menu.items()) {
            final var bounds = layouter.calculateBounds(item, menu, canvas.bounds());
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
}

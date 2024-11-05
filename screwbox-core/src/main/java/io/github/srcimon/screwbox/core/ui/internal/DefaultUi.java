package io.github.srcimon.screwbox.core.ui.internal;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;
import io.github.srcimon.screwbox.core.scenes.internal.DefaultScenes;
import io.github.srcimon.screwbox.core.ui.KeyboardInteractor;
import io.github.srcimon.screwbox.core.ui.SimpleUiLayouter;
import io.github.srcimon.screwbox.core.ui.SimpleUiRenderer;
import io.github.srcimon.screwbox.core.ui.Ui;
import io.github.srcimon.screwbox.core.ui.UiInteractor;
import io.github.srcimon.screwbox.core.ui.UiLayouter;
import io.github.srcimon.screwbox.core.ui.UiMenu;
import io.github.srcimon.screwbox.core.ui.UiRenderer;

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

    public DefaultUi(final Engine engine, final DefaultScenes scenes, final Canvas canvas) {
        this.engine = engine;
        this.scenes = scenes;
        this.canvas = canvas;
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
    }

    public void renderMenu() {
        final var menu = openMenu.menu;
        if (isNull(menu) || scenes.isShowingLoadingScene()) {
            return;
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

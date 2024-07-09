package io.github.srcimon.screwbox.core.ui.internal;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;
import io.github.srcimon.screwbox.core.scenes.internal.DefaultScenes;
import io.github.srcimon.screwbox.core.ui.*;

import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class DefaultUi implements Ui, Updatable {

    private final Engine engine;
    private final DefaultScenes scenes;

    private UiRenderer renderer = new SimpleUiRenderer();
    private UiInteractor interactor = new KeyboardInteractor();
    private UiLayouter layouter = new SimpleUiLayouter();

    private OpenMenu openMenu = new OpenMenu(null, null);

    private record OpenMenu(UiMenu menu, OpenMenu previous) {
    }

    public DefaultUi(final Engine engine, DefaultScenes scenes) {
        this.engine = engine;
        this.scenes = scenes;
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
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        final var menu = openMenu.menu;
        if (nonNull(menu) && !scenes.isShowingLoadingScene()) {
            interactor.interactWith(menu, layouter, engine);
            if (!menu.isActive(menu.selectedItem(), engine)) {
                menu.nextItem(engine);
            }
            renderMenu(menu, engine.graphics().screen());
        }
    }

    private void renderMenu(final UiMenu menu, final Screen screen) {
        for (final var item : menu.items()) {
            final var bounds = layouter.calculateBounds(item, menu, screen);
            if (screen.isVisible(bounds)) {
                String label = item.label(engine);
                if (menu.isSelectedItem(item)) {
                    renderer.renderSelectedItem(label, bounds, screen);
                } else if (menu.isActive(item, engine)) {
                    renderer.renderSelectableItem(label, bounds, screen);
                } else {
                    renderer.renderInactiveItem(label, bounds, screen);
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

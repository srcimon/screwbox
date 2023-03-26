package io.github.simonbas.screwbox.core.ui.internal;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.graphics.Screen;
import io.github.simonbas.screwbox.core.loop.internal.Updatable;
import io.github.simonbas.screwbox.core.ui.*;

import java.util.Optional;

public class DefaultUi implements Ui, Updatable {

    private final Engine engine;

    private UiRenderer renderer = new SimpleUiRenderer();
    private UiInteractor interactor = new KeyboardInteractor();
    private UiLayouter layouter = new SimpleUiLayouter();

    private UiMenu currentMenu = null;

    public DefaultUi(final Engine engine) {
        this.engine = engine;
    }

    @Override
    public Ui openMenu(final UiMenu menu) {
        currentMenu = menu;
        return this;
    }

    @Override
    public void update() {
        if (currentMenu.isPresent()) {
            final var menu = currentMenu.get();
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
        currentMenu = null;
        return this;
    }

    @Override
    public Optional<UiMenu> currentMenu() {
        return Optional.ofNullable(currentMenu);
    }

    @Override
    public Ui setLayouter(final UiLayouter layouter) {
        this.layouter = layouter;
        return this;
    }

}

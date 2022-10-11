package de.suzufa.screwbox.core.ui.internal;

import java.util.Optional;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.graphics.Window;
import de.suzufa.screwbox.core.loop.internal.Updatable;
import de.suzufa.screwbox.core.ui.KeyboardInteractor;
import de.suzufa.screwbox.core.ui.SimpleUiLayouter;
import de.suzufa.screwbox.core.ui.SimpleUiRenderer;
import de.suzufa.screwbox.core.ui.Ui;
import de.suzufa.screwbox.core.ui.UiInteractor;
import de.suzufa.screwbox.core.ui.UiLayouter;
import de.suzufa.screwbox.core.ui.UiMenu;
import de.suzufa.screwbox.core.ui.UiRenderer;

public class DefaultUi implements Ui, Updatable {

    private final Engine engine;

    private UiRenderer renderer = new SimpleUiRenderer();
    private UiInteractor interactor = new KeyboardInteractor();
    private UiLayouter layouter = new SimpleUiLayouter();

    private Optional<UiMenu> currentMenu = Optional.empty();

    public DefaultUi(final Engine engine) {
        this.engine = engine;
    }

    @Override
    public Ui openMenu(final UiMenu menu) {
        currentMenu = Optional.of(menu);
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
            renderMenu(menu, engine.graphics().window());
        }
    }

    private void renderMenu(final UiMenu menu, final Window window) {
        for (final var item : menu.items()) {
            final var bounds = layouter.calculateBounds(item, menu, window);
            if (window.isVisible(bounds)) {
                String label = item.label(engine);
                if (menu.isSelectedItem(item)) {
                    renderer.renderSelectedItem(label, bounds, window);
                } else if (menu.isActive(item, engine)) {
                    renderer.renderSelectableItem(label, bounds, window);
                } else {
                    renderer.renderInactiveItem(label, bounds, window);
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
        currentMenu = Optional.empty();
        return this;
    }

    @Override
    public Ui setLayouter(final UiLayouter layouter) {
        this.layouter = layouter;
        return this;
    }

}

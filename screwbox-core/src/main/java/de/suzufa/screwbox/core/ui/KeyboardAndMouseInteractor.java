package de.suzufa.screwbox.core.ui;

import de.suzufa.screwbox.core.Engine;

public class KeyboardAndMouseInteractor extends KeyboardInteractor {

    @Override
    public void interactWith(final UiMenu menu, final UiLayouter layouter, final Engine engine) {
        for (final var item : menu.items()) {
            final var menuItemBounds = layouter.calculateBounds(item, menu, engine.graphics().screen());
            if (menuItemBounds.contains(engine.mouse().position())) {
                if (item.isActive(engine)) {
                    menu.selectItem(item);
                    if (engine.mouse().justClickedLeft()) {
                        item.trigger(engine);
                    }
                }
            }
        }
        super.interactWith(menu, layouter, engine);
    }

}

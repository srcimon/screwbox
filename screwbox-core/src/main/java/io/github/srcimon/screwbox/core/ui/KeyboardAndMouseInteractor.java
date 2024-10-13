package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.Engine;

public class KeyboardAndMouseInteractor extends KeyboardInteractor {

    @Override
    public void interactWith(final UiMenu menu, final UiLayouter layouter, final Engine engine) {
        for (final var item : menu.items()) {
            final var menuItemBounds = layouter.calculateBounds(item, menu, engine.graphics().screen().size());
            if (menuItemBounds.contains(engine.mouse().offset()) && item.isActive(engine)) {
                menu.selectItem(item);
                if (engine.mouse().isPressedLeft()) {
                    item.trigger(engine);
                }
            }
        }
        super.interactWith(menu, layouter, engine);
    }

}

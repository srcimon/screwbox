package dev.screwbox.core.ui.presets;

import dev.screwbox.core.Engine;
import dev.screwbox.core.ui.UiLayouter;
import dev.screwbox.core.ui.UiMenu;

public class KeyboardAndMouseInteractor extends KeyboardInteractor {

    @Override
    public void interactWith(final UiMenu menu, final UiLayouter layouter, final Engine engine) {
        for (final var item : menu.items()) {
            final var menuItemBounds = layouter.layout(item, menu, engine.graphics().canvas().bounds());
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

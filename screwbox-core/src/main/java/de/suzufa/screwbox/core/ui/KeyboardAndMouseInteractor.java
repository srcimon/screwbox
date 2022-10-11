package de.suzufa.screwbox.core.ui;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.mouse.MouseButton;

public class KeyboardAndMouseInteractor extends KeyboardInteractor {

    @Override
    public void interactWith(final UiMenu menu, final UiLayouter layouter, final Engine engine) {
        for (final var item : menu.items()) {
            final var menuItemBounds = layouter.calculateBounds(item, menu, engine.graphics().window());
            if (menuItemBounds.contains(engine.mouse().position())) {
                menu.selectItem(item);
                if (engine.mouse().justClicked(MouseButton.LEFT)) {
                    item.trigger(engine);
                }
            }
        }
        super.interactWith(menu, layouter, engine);
    }

}

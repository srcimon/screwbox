package dev.screwbox.core.ui.presets;

import dev.screwbox.core.Engine;
import dev.screwbox.core.ui.UiMenu;

public class KeyboardAndMouseInteractor extends KeyboardInteractor {

    @Override
    public void interactWith(final UiMenu menu, final Engine engine) {
        engine.ui().findMenuItem(engine.mouse().offset()).ifPresent(menuItem -> {
            if (menuItem.isActive(engine)) {
                menu.selectItem(menuItem);
                if (engine.mouse().isPressedLeft()) {
                    menuItem.trigger(engine);
                }
            }
        });
        super.interactWith(menu, engine);
    }

}

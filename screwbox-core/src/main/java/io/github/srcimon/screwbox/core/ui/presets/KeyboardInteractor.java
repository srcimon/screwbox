package io.github.srcimon.screwbox.core.ui.presets;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.core.ui.UiInteractor;
import io.github.srcimon.screwbox.core.ui.UiLayouter;
import io.github.srcimon.screwbox.core.ui.UiMenu;

public class KeyboardInteractor implements UiInteractor {

    @Override
    public void interactWith(final UiMenu menu, final UiLayouter layouter, final Engine engine) {
        final var keyboard = engine.keyboard();

        if (keyboard.isPressed(Key.ARROW_DOWN)) {
            menu.nextItem(engine);
        }
        if (keyboard.isPressed(Key.ARROW_UP)) {
            menu.previousItem(engine);
        }
        if (keyboard.isPressed(Key.ENTER)) {
            menu.selectedItem().trigger(engine);
        }
        if (keyboard.isPressed(Key.ESCAPE)) {
            menu.onExit(engine);
        }
    }

}

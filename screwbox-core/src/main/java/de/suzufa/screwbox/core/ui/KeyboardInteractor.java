package de.suzufa.screwbox.core.ui;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.keyboard.Key;

public class KeyboardInteractor implements UiInteractor {

    @Override
    public void interactWith(final UiMenu menu, final UiLayouter layouter, final Engine engine) {
        final var keyboard = engine.keyboard();

        if (keyboard.justPressed(Key.ARROW_DOWN)) {
            menu.nextItem(engine);
        }
        if (keyboard.justPressed(Key.ARROW_UP)) {
            menu.previousItem(engine);
        }
        if (keyboard.justPressed(Key.ENTER)) {
            menu.selectedItem().trigger(engine);
        }
        if (keyboard.justPressed(Key.ESCAPE)) {
            menu.onExit(engine);
        }
    }

}

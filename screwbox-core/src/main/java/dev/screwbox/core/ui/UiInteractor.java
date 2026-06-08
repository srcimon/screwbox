package dev.screwbox.core.ui;

import dev.screwbox.core.Engine;

@FunctionalInterface
public interface UiInteractor {

    void interactWith(UiMenu menu, UiLayout layout, Engine engine);

}

package dev.screwbox.core.ui;

import dev.screwbox.core.Engine;

//TODO document
@FunctionalInterface
public interface UiInteractor {

    void interactWith(UiMenu menu, Engine engine);

}

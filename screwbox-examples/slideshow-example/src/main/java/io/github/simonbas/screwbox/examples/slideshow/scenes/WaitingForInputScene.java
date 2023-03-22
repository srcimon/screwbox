package io.github.simonbas.screwbox.examples.slideshow.scenes;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.scenes.Scene;
import io.github.simonbas.screwbox.core.ui.UiMenu;

public class WaitingForInputScene implements Scene {

    @Override
    public void onEnter(Engine engine) {
        var menu = new UiMenu();
        menu.addItem("Drop PDF-Document on this Window");
        engine.ui().openMenu(menu);
    }

    @Override
    public void onExit(Engine engine) {
        engine.ui().closeMenu();
    }
}

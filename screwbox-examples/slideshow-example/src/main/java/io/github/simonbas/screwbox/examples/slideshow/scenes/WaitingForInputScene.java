package io.github.simonbas.screwbox.examples.slideshow.scenes;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.scenes.Scene;
import io.github.simonbas.screwbox.core.ui.UiMenu;
import io.github.simonbas.screwbox.core.window.WindowDropListener;

import static java.util.Objects.nonNull;

public class WaitingForInputScene implements Scene {

    private WindowDropListener dropListener = null;

    @Override
    public void onEnter(Engine engine) {
        var menu = new UiMenu();
        menu.addItem("Drop PDF-Document on this Window");
        engine.ui().openMenu(menu);

        dropListener = dropEvent -> engine.scenes()
                .addOrReplace(new InputReceivedScene(dropEvent.position(), dropEvent.files()))
                .switchTo(InputReceivedScene.class);

        engine.window().addDropListener(dropListener);
    }

    @Override
    public void onExit(Engine engine) {
        engine.ui().closeMenu();
        if (nonNull(dropListener)) {
            engine.window().removeDropListener(dropListener);
        }
    }
}

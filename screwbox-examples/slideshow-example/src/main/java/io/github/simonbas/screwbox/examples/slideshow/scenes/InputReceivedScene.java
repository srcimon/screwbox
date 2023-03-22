package io.github.simonbas.screwbox.examples.slideshow.scenes;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.scenes.Scene;
import io.github.simonbas.screwbox.core.ui.UiMenu;

import java.io.File;
import java.util.List;

public class InputReceivedScene implements Scene {

    private final List<File> inputFiles;

    public InputReceivedScene(List<File> inputFiles) {
        this.inputFiles = inputFiles;
    }

    @Override
    public void onEnter(Engine engine) {
        var menu = new UiMenu();
        menu.addItem("preparing slideshow").activeCondition(e -> false);
        engine.ui().openMenu(menu);

        engine.async().run(this, new Runnable() {
            @Override
            public void run() {
                for (var file : inputFiles) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    engine.ui().currentMenu().get().addItem("loaded... " + file.getName());
                }
            }
        });

    }
}

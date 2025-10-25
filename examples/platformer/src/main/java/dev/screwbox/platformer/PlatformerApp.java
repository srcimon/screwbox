package dev.screwbox.platformer;

import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.ui.presets.WobblyUiLayout;
import dev.screwbox.platformer.scenes.PauseScene;
import dev.screwbox.platformer.scenes.StartScene;

public class PlatformerApp {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Platformer");

        engine.achievements()
                .addAllFromClassPackage(PlatformerApp.class);

        engine.audio().configuration()
                .setSoundRange(200);

        engine.graphics().camera()
                .setZoomRestriction(2, 10)
                .setZoom(3.0);

        engine.ui()
                .setLayout(new WobblyUiLayout());

        engine.assets()
                .enableLogging()
                .prepareEngineAssetsAsync()
                .prepareClassPackageAsync(PlatformerApp.class);

        engine.scenes()
                .add(new PauseScene())
                .add(new StartScene())
                .switchTo(StartScene.class);

        engine.start();
    }
}
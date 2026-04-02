package dev.screwbox.vacuum;

import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.scenes.SceneTransition;
import dev.screwbox.core.scenes.animations.GridAnimation;
import dev.screwbox.vacuum.scenes.GameScene;

public class VacuumOutlawApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Vacuum Outlaw");

        screwBox.window().enableCursorLock(16);

        screwBox.assets()
            .enableLogging()
            .prepareClassPackageAsync(VacuumOutlawApp.class)
            .prepareEngineAssetsAsync();

        screwBox.scenes()
            .setDefaultTransition(SceneTransition.custom()
                .outroDurationMillis(500)
                .outroAnimation(new GridAnimation())
                .introDurationMillis(500)
                .introAnimation(new GridAnimation()))
            .add(new GameScene())
            .switchTo(GameScene.class, SceneTransition.custom()
                .introDurationMillis(500)
                .introAnimation(new GridAnimation()));

        screwBox.start();
    }
}

package io.github.srcimon.screwbox.vacuum;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.scenes.SceneTransition;
import io.github.srcimon.screwbox.core.scenes.animations.CirclesAnimation;
import io.github.srcimon.screwbox.vacuum.scenes.GameScene;

public class VacuumOutlawApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Vacuum Outlaw");
        screwBox.assets()
                .enableLogging()
                .prepareClassPackageAsync(VacuumOutlawApp.class)
                .prepareEngineAssetsAsync();

        screwBox.scenes()
                .setDefaultTransition(SceneTransition.custom()
                        .outroDurationMillis(500)
                        .outroAnimation(new CirclesAnimation())
                        .introDurationMillis(500)
                        .introAnimation(new CirclesAnimation()))
                .add(new GameScene())
                .switchTo(GameScene.class, SceneTransition.custom()
                        .introDurationMillis(500)
                        .introAnimation(new CirclesAnimation()));

        screwBox.start();
    }
}

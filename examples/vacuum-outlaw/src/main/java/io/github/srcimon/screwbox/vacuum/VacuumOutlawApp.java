package io.github.srcimon.screwbox.vacuum;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.scenes.SceneTransition;
import io.github.srcimon.screwbox.core.scenes.animations.CirclesAnimation;
import io.github.srcimon.screwbox.vacuum.scenes.GameScene;

public class VacuumOutlawApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Vacuum Outlaws");
        screwBox.assets()
                .prepareClassPackageAsync(VacuumOutlawApp.class)
                .prepareEngineAssetsAsync();
        screwBox.scenes()
                .add(new GameScene())
                .switchTo(GameScene.class, SceneTransition.custom()
                        .introDurationSeconds(1)
                        .introAnimation(new CirclesAnimation()));
        screwBox.start();
    }
}

package io.github.srcimon.screwbox.vacuum;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.EngineSetup;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.scenes.SceneTransition;
import io.github.srcimon.screwbox.core.scenes.animations.CirclesAnimation;
import io.github.srcimon.screwbox.vacuum.scenes.GameScene;

import static io.github.srcimon.screwbox.core.EngineSetup.setupAssets;
import static io.github.srcimon.screwbox.core.EngineSetup.setupScenes;

public class VacuumOutlawApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Vacuum Outlaw");
        screwBox.assets()
                .enableLogging()
                .prepareClassPackageAsync(VacuumOutlawApp.class)
                .prepareEngineAssetsAsync();

        screwBox.scenes()
                .add(new GameScene())
                .switchTo(GameScene.class, SceneTransition.custom()
                        .introDurationMillis(500)
                        .introAnimation(new CirclesAnimation()));

        screwBox.start();

        ScrewBox.createEngine("Name",
                        setupScenes(scenes -> scenes
                                .add(new GameScene())
                                .switchTo(GameScene.class, SceneTransition.custom()
                                        .introDurationMillis(500)
                                        .introAnimation(new CirclesAnimation()))),
                        setupAssets(assets -> assets
                                .enableLogging()
                                .prepareClassPackageAsync(VacuumOutlawApp.class)
                                .prepareEngineAssetsAsync()))
                .start();

        //TODO EngineSetupBundle
        var setupEverythingButLight = EngineSetup.combine(
                setupAssets(value -> value.enableLogging()),
                setupScenes(scenes -> scenes.add(new GameScene())));
    }
}

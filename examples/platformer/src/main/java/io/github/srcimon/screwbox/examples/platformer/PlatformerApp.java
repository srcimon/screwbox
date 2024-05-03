package io.github.srcimon.screwbox.examples.platformer;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.ui.WobblyUiLayouter;
import io.github.srcimon.screwbox.examples.platformer.scenes.DeadScene;
import io.github.srcimon.screwbox.examples.platformer.scenes.PauseScene;
import io.github.srcimon.screwbox.examples.platformer.scenes.StartScene;

import static io.github.srcimon.screwbox.core.assets.SceneTransitionBundle.FADEOUT_SLOW;

public class PlatformerApp {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Platformer");
        engine.graphics().camera()
                .setZoomRestriction(2, 5)
                .setZoom(3.0);

        engine.ui().setLayouter(new WobblyUiLayouter());

        engine.assets()
                .enableLogging()
                .prepareEngineAssetsAsync()
                .prepareClassPackageAsync(PlatformerApp.class);

        engine.scenes()
                .setDefaultTransition(FADEOUT_SLOW)
                .add(new DeadScene())
                .add(new PauseScene())
                .add(new StartScene());

        engine.start(StartScene.class);
    }
}
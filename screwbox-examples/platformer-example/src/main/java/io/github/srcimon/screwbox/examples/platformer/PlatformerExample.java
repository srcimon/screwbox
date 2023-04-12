package io.github.srcimon.screwbox.examples.platformer;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.ui.WobblyUiLayouter;
import io.github.srcimon.screwbox.examples.platformer.scenes.DeadScene;
import io.github.srcimon.screwbox.examples.platformer.scenes.PauseScene;
import io.github.srcimon.screwbox.examples.platformer.scenes.StartScene;

public class PlatformerExample {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Platformer Example");

        engine.ui().setLayouter(new WobblyUiLayouter());

        engine.assets()
                .enableLogging()
                .prepareClassPackageAsync(PlatformerExample.class);

        engine.scenes()
                .add(new DeadScene())
                .add(new PauseScene())
                .add(new StartScene());

        engine.start(StartScene.class);
    }
}
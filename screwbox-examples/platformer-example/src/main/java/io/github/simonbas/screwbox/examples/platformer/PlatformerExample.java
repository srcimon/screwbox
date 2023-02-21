package io.github.simonbas.screwbox.examples.platformer;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.ScrewBox;
import io.github.simonbas.screwbox.core.ui.WobblyUiLayouter;
import io.github.simonbas.screwbox.examples.platformer.scenes.DeadScene;
import io.github.simonbas.screwbox.examples.platformer.scenes.PauseScene;
import io.github.simonbas.screwbox.examples.platformer.scenes.StartScene;

public class PlatformerExample {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Platformer Example");

        engine.ui().setLayouter(new WobblyUiLayouter());

        engine.assets()
                .enableLogging()
                .preparePackageAsync("io.github.simonbas.screwbox.examples.platformer");

        engine.scenes()
                .add(new DeadScene())
                .add(new PauseScene())
                .add(new StartScene());

        engine.start(StartScene.class);
    }
}
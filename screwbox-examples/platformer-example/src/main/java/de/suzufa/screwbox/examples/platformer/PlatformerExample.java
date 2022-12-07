package de.suzufa.screwbox.examples.platformer;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.ScrewBox;
import de.suzufa.screwbox.core.ui.WobblyUiLayouter;
import de.suzufa.screwbox.examples.platformer.scenes.DeadScene;
import de.suzufa.screwbox.examples.platformer.scenes.PauseScene;
import de.suzufa.screwbox.examples.platformer.scenes.StartScene;

public class PlatformerExample {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Platformer Example");

        engine.ui().setLayouter(new WobblyUiLayouter());

        engine.assets()
                .enableLogging()
                .preparePackageAsync("de.suzufa.screwbox.examples.platformer");

        engine.scenes()
                .add(new DeadScene())
                .add(new PauseScene())
                .add(new StartScene());

        engine.start(StartScene.class);
    }
}
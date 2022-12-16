package de.suzufa.screwbox.examples.platformer;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.ScrewBox;
import de.suzufa.screwbox.core.graphics.MouseCursor;
import de.suzufa.screwbox.core.ui.WobblyUiLayouter;
import de.suzufa.screwbox.examples.platformer.scenes.DeadScene;
import de.suzufa.screwbox.examples.platformer.scenes.PauseScene;
import de.suzufa.screwbox.examples.platformer.scenes.StartScene;

public class PlatformerExample {
// --add-opens java.desktop/com.apple.eawt=ALL-UNNAMED
    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Platformer Example");

        engine.ui().setLayouter(new WobblyUiLayouter());
        engine.graphics().window().setCursor(MouseCursor.DEFAULT);
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
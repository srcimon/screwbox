package de.suzufa.screwbox.playground.debo;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.ScrewBox;
import de.suzufa.screwbox.core.ui.WobblyUiLayouter;
import de.suzufa.screwbox.playground.debo.scenes.DeadScene;
import de.suzufa.screwbox.playground.debo.scenes.PauseScene;
import de.suzufa.screwbox.playground.debo.scenes.StartScene;

public class DeboApplication {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine();

        engine.ui().setLayouter(new WobblyUiLayouter());

        engine.graphics().configuration().setFullscreen(true);

        engine.scenes()
                .add(new DeadScene())
                .add(new PauseScene())
                .add(new StartScene());

        engine.start(StartScene.class);
    }

}

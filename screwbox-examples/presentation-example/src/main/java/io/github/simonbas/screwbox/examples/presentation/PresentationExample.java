package io.github.simonbas.screwbox.examples.presentation;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.ScrewBox;
import io.github.simonbas.screwbox.core.ui.WobblyUiLayouter;
import io.github.simonbas.screwbox.examples.presentation.scenes.WaitingForInputScene;

public class PresentationExample {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Presentation Example");

        engine.scenes().add(new WaitingForInputScene());

        engine.ui().setLayouter(new WobblyUiLayouter());

        engine.start(WaitingForInputScene.class);
    }

}

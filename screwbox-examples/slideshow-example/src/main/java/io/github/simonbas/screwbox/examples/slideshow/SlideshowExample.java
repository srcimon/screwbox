package io.github.simonbas.screwbox.examples.slideshow;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.ScrewBox;
import io.github.simonbas.screwbox.core.scenes.Scenes;
import io.github.simonbas.screwbox.core.ui.WobblyUiLayouter;
import io.github.simonbas.screwbox.examples.slideshow.scenes.InputReceivedScene;
import io.github.simonbas.screwbox.examples.slideshow.scenes.WaitingForInputScene;

public class SlideshowExample {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Slideshow Example");

        Scenes scenes = engine.scenes();

        scenes.add(new WaitingForInputScene());

        engine.ui().setLayouter(new WobblyUiLayouter());

        engine.window().addDropListener(dropEvent -> scenes
                .addOrReplace(new InputReceivedScene(dropEvent.files()))
                .switchTo(InputReceivedScene.class));

        engine.start(WaitingForInputScene.class);
    }

}

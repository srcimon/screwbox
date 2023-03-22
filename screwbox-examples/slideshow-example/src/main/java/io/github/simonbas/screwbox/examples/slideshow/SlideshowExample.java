package io.github.simonbas.screwbox.examples.slideshow;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.ScrewBox;
import io.github.simonbas.screwbox.core.ui.WobblyUiLayouter;
import io.github.simonbas.screwbox.examples.slideshow.scenes.InputReceivedScene;
import io.github.simonbas.screwbox.examples.slideshow.scenes.WaitingForInputScene;

public class SlideshowExample {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Slideshow Example");

        engine.scenes().add(new WaitingForInputScene());

        engine.ui().setLayouter(new WobblyUiLayouter());
        engine.window().addDropListener(dropEvent -> {
            if (engine.scenes().isActive(InputReceivedScene.class)) {
                engine.scenes().switchTo(WaitingForInputScene.class);
                engine.scenes().remove(InputReceivedScene.class);
            }
            engine.scenes().add(new InputReceivedScene(dropEvent.files()));
            engine.scenes().switchTo(InputReceivedScene.class);
        });
        engine.start(WaitingForInputScene.class);
    }
}

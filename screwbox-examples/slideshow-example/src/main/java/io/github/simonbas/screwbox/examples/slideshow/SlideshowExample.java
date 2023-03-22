package io.github.simonbas.screwbox.examples.slideshow;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.ScrewBox;
import io.github.simonbas.screwbox.core.scenes.Scenes;
import io.github.simonbas.screwbox.core.ui.WobblyUiLayouter;
import io.github.simonbas.screwbox.core.window.WindowDropEvent;
import io.github.simonbas.screwbox.examples.slideshow.scenes.InputReceivedScene;
import io.github.simonbas.screwbox.examples.slideshow.scenes.WaitingForInputScene;

public class SlideshowExample {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Slideshow Example");

        Scenes scenes = engine.scenes();

        scenes.add(new WaitingForInputScene());

        engine.ui().setLayouter(new WobblyUiLayouter());
        engine.window().addDropListener(dropEvent -> switchToInputReceived(scenes, dropEvent));
        engine.start(WaitingForInputScene.class);
    }

    private static void switchToInputReceived(Scenes scenes, WindowDropEvent dropEvent) {
        if (scenes.isActive(InputReceivedScene.class)) {
            scenes.switchTo(WaitingForInputScene.class);
            scenes.remove(InputReceivedScene.class);
        }
        scenes.add(new InputReceivedScene(dropEvent.files()));
        scenes.switchTo(InputReceivedScene.class);
    }
}

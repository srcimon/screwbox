package io.github.simonbas.screwbox.examples.slideshow;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.ScrewBox;
import io.github.simonbas.screwbox.core.ui.WobblyUiLayouter;
import io.github.simonbas.screwbox.examples.slideshow.scenes.WaitingForInputScene;

public class SlideshowExample {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Slideshow Example");

        engine.scenes().add(new WaitingForInputScene());
        engine.graphics().restrictZoomRangeTo(0.1, 8);
        engine.loop().setTargetFps(9999);
        engine.ui().setLayouter(new WobblyUiLayouter());

        engine.start(WaitingForInputScene.class);
    }

}

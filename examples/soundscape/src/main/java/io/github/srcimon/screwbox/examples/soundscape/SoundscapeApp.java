package io.github.srcimon.screwbox.examples.soundscape;

import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.audio.Sound;

//TODO Document in readme.md
//TODO fix Audio Feature description in readme.md
public class SoundscapeApp {

    public static void main(String[] args) {
        var screwBox = ScrewBox.createEngine("Soundscape");

        screwBox.environment().addSystem(engine -> {
            if (engine.mouse().isPressedLeft()) {
                engine.audio().playEffect(Sound.dummyEffect(), engine.mouse().position());
            }
        });

        screwBox.start();
    }
}

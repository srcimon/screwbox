package io.github.srcimon.screwbox.examples.soundscape;

import io.github.srcimon.screwbox.core.ScrewBox;

//TODO Document in readme.md
//TODO fix Audio Feature description in readme.md
public class SoundscapeApp {

    public static void main(String[] args) {
        var screwBox = ScrewBox.createEngine("Soundscape");

        screwBox.environment()
                .addSystem(new PlaySoundOnClickSystem())
                .addSystem(new VisualizePlaybacksSystem());

        screwBox.start();
    }
}

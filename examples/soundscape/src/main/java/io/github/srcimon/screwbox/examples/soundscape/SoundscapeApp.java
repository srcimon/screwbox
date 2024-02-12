package io.github.srcimon.screwbox.examples.soundscape;

import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.environment.debug.LogFpsSystem;
import io.github.srcimon.screwbox.core.graphics.Color;

//TODO Document in readme.md
//TODO fix Audio Feature description in readme.md
public class SoundscapeApp {

    public static void main(String[] args) {
        var screwBox = ScrewBox.createEngine("Soundscape");

        screwBox.environment()
                .addSystem(new LogFpsSystem())
                .addSystem(engine -> {
                    if (engine.mouse().isPressedLeft()) {
                        engine.audio().playEffect(Sound.dummyEffect(), engine.mouse().position());
                    }
                }).addSystem(engine -> {
                    engine.audio().activePlaybacks().stream().filter(playback -> playback.position().isPresent())
                            .forEach(playback -> engine.graphics().world().drawCircle(playback.position().get(),
                                    engine.audio().configuration().soundDistance() * playback.done().value(), Color.RED.opacity(playback.done().invert()), 8));
                });


        screwBox.start();
    }
}

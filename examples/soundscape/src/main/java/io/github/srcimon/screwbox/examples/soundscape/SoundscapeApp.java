package io.github.srcimon.screwbox.examples.soundscape;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.graphics.Color;

//TODO Document in readme.md
//TODO fix Audio Feature description in readme.md
public class SoundscapeApp {

    public static void main(String[] args) {
        var screwBox = ScrewBox.createEngine("Soundscape");

        screwBox.environment()
                .addSystem(engine -> //
                        engine.graphics().screen().drawTextCentered(engine.graphics().screen().center(), "Click to play sound", 2)
                )
                .addSystem(engine -> { // play sound effect on mouse position
                    if (engine.mouse().isPressedLeft()) {
                        engine.audio().playEffect(Sound.dummyEffect(), engine.mouse().position());
                    }
                }).addSystem(engine -> { // visualize active sound effects
                    for (var playback : engine.audio().activePlaybacks()) {
                        Percent percentDone = Percent.of(1.0 * Duration.since(playback.start()).milliseconds()/ playback.sound().duration().milliseconds());
                        engine.graphics().world().drawCircle(
                                playback.position().get(),
                                engine.audio().configuration().soundDistance() * percentDone.value(),
                                Color.BLUE.opacity(percentDone.invert()),
                                8);
                    }
                });


        screwBox.start();
    }
}

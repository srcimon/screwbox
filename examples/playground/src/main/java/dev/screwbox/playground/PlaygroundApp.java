package dev.screwbox.playground;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.assets.FontBundle;
import dev.screwbox.core.audio.Playback;
import dev.screwbox.core.audio.Voice;
import dev.screwbox.core.graphics.ShaderBundle;
import dev.screwbox.core.graphics.options.TextDrawOptions;
import dev.screwbox.core.utils.ListUtil;

import java.util.List;

public class PlaygroundApp {

    static String text = null;

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");

        List<String> texts = List.of(
            "I am your friend. But i am also okay with it.",
            "My cat is so fat, it cannot swim in the ocean.",
            "Lets have a party.",
            "Do you want to be my friend?",
            "Is this your house?",
            "Can I ask you a question? It's about life and death."
        );


        screwBox.environment().addSystem(engine -> {
            if (playback == null || !engine.audio().isPlaybackActive(playback)) {
                text = null;
            }
            if (text != null) {
                engine.graphics().canvas().drawText(engine.graphics().canvas().center(), text, TextDrawOptions.font(FontBundle.SKINNY_SANS).scale(3).alignCenter().shader(ShaderBundle.NEON));
            }

            if (engine.mouse().isPressedLeft()) {
                text = ListUtil.pickRandom(texts);
                playback = engine.audio().playChatter(text, Voice.variable()
                    .baseFrequency(220.0)
                    .baseBlipDuration(Duration.ofMillis(80))
                    .commaPause(Duration.ofMillis(100))
                    .wordPause(Duration.ofMillis(80))
                    .sentencePause(Duration.ofMillis(250))
                    .vibratoDepth(Percent.of(0.0))
                    .vibratoSpeed(1.0)
                    .nasalVolume(Percent.of(0.1))
                    .vowelVolume(Percent.of(0.95))
                    .waveType(Voice.WaveType.SQUARE)
                    .pitchRandomness(Percent.of(0.0))
                    .scratchyness(Percent.of(0.15))
                    .speedRandomness(Percent.of(0.0)));

            }
        });

        screwBox.start();
    }

    static Playback playback;

}
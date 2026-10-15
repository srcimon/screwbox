package dev.screwbox.playground;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Time;
import dev.screwbox.core.assets.FontBundle;
import dev.screwbox.core.audio.MumbleSynthesizer;
import dev.screwbox.core.graphics.ShaderBundle;
import dev.screwbox.core.graphics.options.TextDrawOptions;
import dev.screwbox.core.utils.ListUtil;

import java.util.List;

public class PlaygroundApp {

    static Time timeout = Time.now();
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
            if (Time.now().isAfter(timeout)) {
                text = null;
            }
            if (text != null) {
                engine.graphics().canvas().drawText(engine.graphics().canvas().center(), text, TextDrawOptions.font(FontBundle.SKINNY_SANS).scale(3).alignCenter().shader(ShaderBundle.NEON));
            }
            if (engine.mouse().isPressedLeft()) {
                text = ListUtil.pickRandom(texts);
                timeout = Time.now().addSeconds(2);
                Time t = Time.now();
                MumbleSynthesizer.VoiceOptions config = new MumbleSynthesizer.VoiceOptions();
                config.baseFreq = 260.0;
                config.baseBlipDuration = 0.05;

                config.pitchRandomness = 0.6;       // Etwas reduziert für stabilere Intonation
                config.speedRandomness = 0.10;

                // Sanftes Vibrato ohne extreme Ausschläge
                config.vibratoSpeed = 5.0;
                config.vibratoDepth = 0.15;

                // Pausenkonfiguration
                config.sentencePause = 0.25;
                config.commaPause = 0.12;
                config.wordPause = 0.08;

                // Mischpult
                config.vowelGain = 0.70;
                config.nasalGain = 0.40;
                config.plosiveGain = 0.30;
                config.waveType = MumbleSynthesizer.WaveType.SINE;

                var mumble = MumbleSynthesizer.synthesizeMumbleSpeech(text, config);
                System.out.println(Duration.since(t).milliseconds());
                engine.audio().playSound(mumble);
            }
        });


        screwBox.start();
    }
}
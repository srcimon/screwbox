package dev.screwbox.playground;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Time;
import dev.screwbox.core.assets.FontBundle;
import dev.screwbox.core.audio.Sound;
import dev.screwbox.core.graphics.ShaderBundle;
import dev.screwbox.core.graphics.options.TextDrawOptions;
import dev.screwbox.core.utils.ListUtil;

import java.util.List;

public class PlaygroundApp {

    static Time timeout = Time.now();
    static String text = null;

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");

        List<String > texts = List.of(
            "I am your friend. But i am also okay with it.",
            "My cat is so fat, it cannot swim in the ocean.",
            "Lets have a party.",
            "Can I ask you a question? It's about life and death."
        );


        screwBox.environment().addSystem(engine -> {
            if(Time.now().isAfter(timeout)) {
                text=null;
            }
            if(text != null) {
                engine.graphics().canvas().drawText(engine.graphics().canvas().center(), text, TextDrawOptions.font(FontBundle.SKINNY_SANS).scale(3).alignCenter().shader(ShaderBundle.NEON));
            }
            if(engine.mouse().isPressedLeft()) {
                text = ListUtil.pickRandom(texts);
                timeout = Time.now().addSeconds(2);
                Time t = Time.now();
                Prototype.VoiceConfig config = new Prototype.VoiceConfig();
                config.baseFreq = 110.0;             // Grundtonhöhe (z.B. 110Hz Mann, 200Hz Frau)
                config.baseBlipDuration = 0.08;      // Sprechgeschwindigkeit

                config.pitchRandomness = 0.15;       // Natürliche Mikro-Schwankung der Tonhöhe
                config.speedRandomness = 0.25;       // Natürliche Schwankung des Sprechtempos

                // Menschliches Vibrato (Muskel-Instabilität)
                config.vibratoSpeed = 5.5;           // Zitter-Geschwindigkeit in Hz
                config.vibratoDepth = 0.025;         // Zitter-Stärke

                // Pausenkonfiguration
                config.sentencePause = 0.45;
                config.commaPause = 0.22;
                config.wordPause = 0.15;

                // Mischpult
                config.noiseGain = 0.015;            // Stark gedämpftes, weiches Rauschen
                config.vowelGain = 0.75;
                config.nasalGain = 0.40;
                config.plosiveGain = 0.35;
                var mumble = Prototype.generateMumbleWavByteArray(text, Prototype.WaveType.SQUARE, config);
                System.out.println(Duration.since(t).milliseconds());
                var mumbleSound = Sound.fromSoundData(mumble);
                engine.audio().playSound(mumbleSound);
            }
        });


        screwBox.start();
    }
}
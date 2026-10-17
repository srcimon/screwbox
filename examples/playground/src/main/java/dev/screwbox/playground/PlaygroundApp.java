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

            var robotVoice = Voice.variable()
                .baseFrequency(220.0)
                .baseBlipDuration(Duration.ofMillis(80))
                .commaPause(Duration.ofMillis(100))
                .wordPause(Duration.ofMillis(80))
                .sentencePause(Duration.ofMillis(250))
                .vibratoDepth(Percent.of(0.0))          // Komplett deaktiviert für einen starren Ton
                .vibratoSpeed(1.0)
                .nasalVolume(Percent.of(0.1))
                .vowelVolume(Percent.of(0.95))          // Nahe 1.0 (95%) für maximale, klare Lautstärke
                .waveType(Voice.WaveType.SQUARE)      // SAWTOOTH erzeugt den perfekten, scharfen Roboter-Synthesizer-Klang
                .pitchRandomness(Percent.of(0.0))      // 0% verhindert menschliche Tonhöhenschwankungen (monoton)
                .scratchyness(Percent.of(0.15))         // Leichtes metallisches Raspeln hinzugefügt
                .speedRandomness(Percent.of(0.0));     // 0% für ein perfekt gleichmäßiges, maschinelles Sprechtempo

            var voice = Voice.variable()
                .baseFrequency(180.0)                   // Eine mittlere bis leicht erhöhte, neutrale Tonhöhe
                .baseBlipDuration(Duration.ofMillis(50))  // Sehr kurze Blips für einen abgehackten, plätschernden Sound
                .commaPause(Duration.ofMillis(100))
                .wordPause(Duration.ofMillis(70))       // Schnelle Wortübergänge wie schnappende Fischmäuler
                .sentencePause(Duration.ofMillis(250))
                .vibratoDepth(Percent.of(0.85))         // Extrem hoch, um die Tonhöhe stark schwanken zu lassen
                .vibratoSpeed(15.0)                     // Sehr schnell eingestellt – das erzeugt den "Unterwasser-Gurgel-Effekt"
                .nasalVolume(Percent.of(0.4))           // Erhöht, damit es dumpf und "unter Wasser" klingt
                .vowelVolume(Percent.of(0.70))
                .waveType(Voice.WaveType.SINE)          // Die weiche Sinuswelle imitiert flüssige, aquatische Töne
                .pitchRandomness(Percent.of(0.35))      // Bringt zusätzliche, unvorhersehbare Bewegung in den Ton
                .scratchyness(Percent.of(0.1))          // Keine Kratzigkeit, damit der Sound "flüssig" bleibt
                .speedRandomness(Percent.of(0.4));      // Hohe Rhythmus-V
            if (engine.mouse().isPressedLeft()) {
                text = ListUtil.pickRandom(texts);
                playback = engine.audio().playChatter(text, voice);

            }
        });

        //TODO VoiceBundle
        //TODO update Audio documentation with voice bundle
        screwBox.start();
    }

    static Playback playback;

}
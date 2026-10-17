package dev.screwbox.playground;

import dev.screwbox.core.Engine;
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
                playback = engine.audio().speakMumbled(text, Voice.variable().baseFrequency(250));
            }
        });

        //TODO update lets go system
//TODO document in changelog: Synthesizer, Audio methods
        //TODO update Audio documentation
        screwBox.start();
    }

    static Playback playback;

}
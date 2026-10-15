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

        List<String > texts = List.of("I am your friend. But i am also okay with it");


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
                var mumble = Prototype.generateMumbleWavByteArray(text, 350, Prototype.WaveType.SINE, 0.015);
                System.out.println(Duration.since(t).milliseconds());
                var mumbleSound = Sound.fromSoundData(mumble);
                engine.audio().playSound(mumbleSound);
            }
        });


        screwBox.start();
    }
}
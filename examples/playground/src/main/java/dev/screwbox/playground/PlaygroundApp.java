package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Time;
import dev.screwbox.core.Vector;
import dev.screwbox.core.assets.FontBundle;
import dev.screwbox.core.audio.Prototype;
import dev.screwbox.core.audio.Sound;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.core.StaticBoundsComponent;
import dev.screwbox.core.environment.importing.ImportOptions;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.environment.smoke.SmokeObstacleComponent;
import dev.screwbox.core.environment.smoke.WindComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ShaderBundle;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.options.TextDrawOptions;
import dev.screwbox.core.graphics.smoke.SmokeOptions;
import dev.screwbox.core.utils.ListUtil;
import dev.screwbox.core.utils.TileMap;

import java.util.List;

public class PlaygroundApp {

    static Time timeout = Time.now();
    static String text = null;

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");

        List<String > texts = List.of("I am your friend", "Hello my little friend", "Hey, whats going on?", "Are you okay", "I want to go to the doctor", "Aliens are attacking", "My cat is fat");


        screwBox.environment().addSystem(engine -> {
            if(Time.now().isAfter(timeout)) {
                text=null;
            }
            if(text != null) {
                engine.graphics().canvas().drawText(engine.graphics().canvas().center(), text, TextDrawOptions.font(FontBundle.ARCADE_SERIF).scale(3).alignCenter().shader(ShaderBundle.NEON));
            }
            if(engine.mouse().isPressedLeft()) {
                text = ListUtil.pickRandom(texts);
                timeout = Time.now().addSeconds(2);
                var mumble = Prototype.generateMumbleWavByteArray(text, 100, Prototype.WaveType.SINE, 0.025);
                var mumbleSound = Sound.fromSoundData(mumble);
                engine.audio().playSound(mumbleSound);
            }
        });


        screwBox.start();
    }
}
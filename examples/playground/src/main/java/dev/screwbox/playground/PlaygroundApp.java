package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
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
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.smoke.SmokeOptions;
import dev.screwbox.core.utils.TileMap;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");

        screwBox.environment().addSystem(engine -> {
            if(engine.mouse().isPressedLeft()) {
                var mumble = Prototype.generateMumbleWavByteArray("This is a really nice example for an artifical noise!", 100, Prototype.WaveType.SINE, 0.04);
                var mumbleSound = Sound.fromSoundData(mumble);
                engine.audio().playSound(mumbleSound);
            }
        });


        screwBox.start();
    }
}
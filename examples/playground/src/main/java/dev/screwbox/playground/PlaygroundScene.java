package dev.screwbox.playground;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Time;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.controls.JumpControlComponent;
import dev.screwbox.core.environment.controls.LeftRightControlComponent;
import dev.screwbox.core.environment.controls.SuspendJumpControlComponent;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.physics.*;
import dev.screwbox.core.environment.rendering.CameraBoundsComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.FloatRotationComponent;
import dev.screwbox.core.environment.rendering.FluidRenderComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.options.RectangleDrawOptions;
import dev.screwbox.core.scenes.Scene;
import dev.screwbox.core.utils.AsciiMap;

public class PlaygroundScene implements Scene {

static Time last = Time.unset();
static Percent value = Percent.half().invert();
    @Override
    public void populate(Environment environment) {


        environment
                .enableAllFeatures()
                .addSystem(engine -> {
                    var ml = engine.audio().microphoneLevel();
                    if(value.value() != ml.value()) {
                        value = ml;
                        last = Time.now();
                    }
                })
                .addSystem(engine -> engine.graphics().canvas().drawRectangle(Offset.origin(), Size.of(engine.audio().microphoneLevel().rangeValue(0, engine.graphics().screen().width()), engine.graphics().screen().height()), RectangleDrawOptions.filled(Color.RED)))
                .addSystem(new LogFpsSystem());
    }
}

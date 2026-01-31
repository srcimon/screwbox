package dev.screwbox.playground;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Line;
import dev.screwbox.core.Percent;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Time;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.keyboard.Key;

public class PlaygroundApp {

    static Angle rotation = Angle.degrees(0);

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");

        engine.graphics().camera().setZoom(4);
        engine.graphics().configuration().setLightQuality(Percent.threeQuarter());
        engine.environment()
            .enableAllFeatures()
            .addSystem(new LogFpsSystem())
            .addSystem(e -> e.graphics().canvas().fillWith(Color.BLUE))
            .addSystem(e -> {
                Line between = rotation.applyOn(Line.between(e.mouse().position(), e.mouse().position().add(50, -10)));
                e.graphics().world().drawLine(between, LineDrawOptions.color(Color.WHITE).drawOrder(Order.DEBUG_OVERLAY_LATE.drawOrder()));
                e.graphics().light().addDirectionalLight(between, 80, Angle.of(between).addDegrees(270 + Math.sin(e.loop().runningTime().milliseconds() / 1000.0) * 45), Color.BLACK);
                if (e.keyboard().isDown(Key.Q)) {
                    rotation = rotation.addDegrees(e.loop().delta(-40));
                }
                if (e.keyboard().isDown(Key.E)) {
                    rotation = rotation.addDegrees(e.loop().delta(40));
                }
            });

        engine.start();
    }

}
package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Line;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.LineDrawOptions;

public class PlaygroundApp {

    static Vector pos;

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");

        engine.graphics().camera().setZoom(4);
        engine.environment()
            .enableAllFeatures()
            .addSystem(new LogFpsSystem())
            .addSystem(e -> e.graphics().canvas().fillWith(Color.BLUE))
            .addSystem(e -> {
                Line between = Line.between(e.mouse().position(), e.mouse().position().add(50, -10));
                e.graphics().world().drawLine(between, LineDrawOptions.color(Color.WHITE).drawOrder(Order.DEBUG_OVERLAY_LATE.drawOrder()));
                e.graphics().light().addDirectionalLight(between, 80, Color.BLACK);
            });

        engine.start();
    }

}
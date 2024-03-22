package io.github.srcimon.screwbox.examples.helloworld;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.graphics.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.Color;

public class HelloWorldApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Hello World");
        screwBox.graphics().configuration().setUseAntialiasing(true);

        screwBox.audio().startInputMonitoring();
        screwBox.environment().addSystem(engine -> {
            var screen = engine.graphics().screen();
            screen.drawCircle(screen.center().addX(-100), (int) (engine.audio().microphoneLevel().value() * 50), CircleDrawOptions.filled(Color.RED));
            screen.drawCircle(screen.center().addX(-100), 50, CircleDrawOptions.outline(Color.WHITE).strokeWidth(2));
            screen.drawCircle(screen.center().addX(100), (int) (engine.audio().smoothedMicrophoneLevel().value() * 50), CircleDrawOptions.filled(Color.RED));
            screen.drawCircle(screen.center().addX(100), 50, CircleDrawOptions.outline(Color.WHITE).strokeWidth(2));
        });

        screwBox.start();
    }
}
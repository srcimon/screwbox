package io.github.srcimon.screwbox.examples.helloworld;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.graphics.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.Color;

public class HelloWorldApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Hello World");
screwBox.audio().startInputMonitoring();
        screwBox.environment().addSystem(engine -> {
            var screen = engine.graphics().screen();
            screen.drawCircle(screen.center(), (int) (engine.audio().microphoneLevel().value() * 50), CircleDrawOptions.filled(Color.RED));
            screen.drawCircle(screen.center(), 50, CircleDrawOptions.outline(Color.WHITE));
        });

        screwBox.start();
    }
}
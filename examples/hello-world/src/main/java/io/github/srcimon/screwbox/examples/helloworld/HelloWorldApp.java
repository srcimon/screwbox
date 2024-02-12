package io.github.srcimon.screwbox.examples.helloworld;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;

public class HelloWorldApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Hello World");

        screwBox.environment().addSystem(engine -> {
            var screen = engine.graphics().screen();
            screen.drawTextCentered(screen.center(), "HELLO WORLD!", 2);
        });

        screwBox.start();
    }
}
package io.github.srcimon.screwbox.examples.helloworld;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.assets.FontsBundle;

//TODO FIX EXAMPLE APP IN README
public class HelloWorldApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Hello World");

        screwBox.environment().addSystem(engine -> {
            var screen = screwBox.graphics().screen();
            screen.drawTextCentered(screen.center(), "Hello world", FontsBundle.BOLDZILLA.white().get(), 4);
        });
        screwBox.start();
    }
}
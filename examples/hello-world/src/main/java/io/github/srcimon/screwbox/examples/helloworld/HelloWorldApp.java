package io.github.srcimon.screwbox.examples.helloworld;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.assets.Fonts;
import io.github.srcimon.screwbox.core.graphics.Color;

public class HelloWorldApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Hello World");


        screwBox.environment().addSystem(engine -> {
            var screen = engine.graphics().screen();
            screen.drawTextCentered(screen.center(), "Aber ABER HELLO WORLD!", Fonts.SKINNY_SANS_WHITE.get(),  4);
        });


        screwBox.start();
    }
}
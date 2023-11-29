package io.github.srcimon.screwbox.examples.helloworld;

import static io.github.srcimon.screwbox.core.graphics.Color.random;
import static io.github.srcimon.screwbox.core.graphics.Pixelfont.defaultFont;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;

public class HelloWorldExample {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Hello World Example");

        screwBox.entities()
                .add(engine -> {
                    var screen = engine.graphics().screen();
                    screen.drawTextCentered(screen.center(), "HELLO WORLD!", defaultFont(random()), 2);
                });

        screwBox.start();
    }
}

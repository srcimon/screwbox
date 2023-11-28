package io.github.srcimon.screwbox.examples.helloworld;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.entities.systems.QuitOnKeyPressSystem;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.keyboard.Key;

import static io.github.srcimon.screwbox.core.graphics.Pixelfont.defaultFont;

public class HelloWorldExample {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Hello World Example");

        screwBox.entities()
                .add(engine -> {
                    Screen screen = engine.graphics().screen();
                    screen.drawTextCentered(screen.center(), "HELLO WORLD!", defaultFont(), 2);
                });

        screwBox.start();
    }
}

package io.github.srcimon.screwbox.examples.helloworld;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;

import static io.github.srcimon.screwbox.core.assets.FontsBundle.BOLDZILLA;
import static io.github.srcimon.screwbox.core.graphics.TextDrawOptions.font;

//TODO fixup readme.md example
public class HelloWorldApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Hello World");

        screwBox.environment().addSystem(engine -> {
            var screen = engine.graphics().screen();
            var drawOptions = font(BOLDZILLA).scale(4).alignCenter();
            screen.drawText(screen.center(), "Hello world!", drawOptions);
        });

        screwBox.start();
    }
}
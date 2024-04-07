package io.github.srcimon.screwbox.examples.helloworld;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.assets.FontsBundle;

import static io.github.srcimon.screwbox.core.assets.FontsBundle.BOLDZILLA;
import static io.github.srcimon.screwbox.core.assets.FontsBundle.SKINNY_SANS;
import static io.github.srcimon.screwbox.core.graphics.TextDrawOptions.font;

//TODO fixup example
//TODO fixup readme.md example
public class HelloWorldApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Hello World");

        screwBox.environment().addSystem(engine -> {
            var screen = screwBox.graphics().screen();
            screen.drawText(screen.center(), "Hello world", font(SKINNY_SANS.white()).padding(1).scale(4).uppercase());
        });
        screwBox.start();
    }
}
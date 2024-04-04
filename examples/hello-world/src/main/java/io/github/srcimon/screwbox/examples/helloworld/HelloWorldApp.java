package io.github.srcimon.screwbox.examples.helloworld;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.assets.Fonts;

public class HelloWorldApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Hello World");


        screwBox.environment().addSystem(engine -> {
            var screen = engine.graphics().screen();
            screen.drawTextCentered(screen.center(), "The quick brown fox jumps over the lazy dog. Jinxed wizards pluck ivy from the big quilt.", Fonts.SKINNY_SANS_WHITE.get(), 2);
            screen.drawTextCentered(screen.center().addY(20), "Jaded zombies acted quaintly but kept driving their dizzy oxen forward.", Fonts.SKINNY_SANS_WHITE.get(), 2);
            screen.drawTextCentered(screen.center().addY(40), "We promptly judged antique ivory buckles for the next prize.", Fonts.SKINNY_SANS_WHITE.get(), 2);
        });

        screwBox.start();
    }
}
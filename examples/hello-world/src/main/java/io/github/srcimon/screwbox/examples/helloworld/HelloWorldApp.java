package io.github.srcimon.screwbox.examples.helloworld;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.assets.ScrewBoxFontBundle;
import io.github.srcimon.screwbox.core.graphics.Color;

public class HelloWorldApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Hello World");


        screwBox.environment().addSystem(engine -> {
            var screen = engine.graphics().screen();
            screen.fillWith(Color.WHITE);
            screen.drawTextCentered(screen.center(), "The quick: brown fox jumps over the lazy dog. Jinxed wizards pluck ivy from the big quilt.", ScrewBoxFontBundle.SKINNY_SANS.get(), 2);
            screen.drawTextCentered(screen.center().addY(20), "Jaded, zombies acted quaintly but kept driving their dizzy oxen forward.", ScrewBoxFontBundle.SKINNY_SANS.get(), 2);
            screen.drawTextCentered(screen.center().addY(40), "We promptly judged antique ivory buckles for the next prize.", ScrewBoxFontBundle.SKINNY_SANS.get(), 2);
            screen.drawTextCentered(screen.center().addY(80), "Current Engine Runtime: " + engine.loop().runningTime().humanReadable(), ScrewBoxFontBundle.SKINNY_SANS.get(), 4);
        });
        screwBox.start();
    }
}
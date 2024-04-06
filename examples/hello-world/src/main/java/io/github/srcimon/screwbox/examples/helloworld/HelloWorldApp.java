package io.github.srcimon.screwbox.examples.helloworld;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.assets.FontsBundle;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Pixelfont;

import static io.github.srcimon.screwbox.core.assets.Asset.asset;

public class HelloWorldApp {

    private static final Asset<Pixelfont> FONT = asset(() -> FontsBundle.BOLDZILLA.white());
    private static final Pixelfont FONT_RED = FontsBundle.BOLDZILLA.customColor(Color.RED);

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Hello World");
        screwBox.assets().enableLogging().prepareClassPackageAsync(HelloWorldApp.class);

        screwBox.environment().addSystem(engine -> {
            var screen = engine.graphics().screen();
            screen.drawTextCentered(screen.center(), "The quick: brown fox jumps over the lazy dog. Jinxed wizards pluck ivy from the big quilt.", FONT.get(), 2);
            screen.drawTextCentered(screen.center().addY(20), "Jaded, zombies acted quaintly but kept driving their dizzy oxen forward.", FONT.get(), 2);
            screen.drawTextCentered(screen.center().addY(40), "We promptly judged antique ivory buckles for the next prize.", FONT.get(), 2);
            screen.drawTextCentered(screen.center().addY(80), "Current Engine Runtime: " + engine.loop().runningTime().humanReadable(), FONT_RED, 4);
        });
        screwBox.start();
    }
}
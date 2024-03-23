package io.github.srcimon.screwbox.examples.helloworld;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.environment.core.QuitOnKeySystem;
import io.github.srcimon.screwbox.core.graphics.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.keyboard.Key;

public class HelloWorldApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Hello World");
        screwBox.graphics().configuration().setUseAntialiasing(true);

        screwBox.environment()
                .addSystem(new QuitOnKeySystem())
                .addSystem(engine -> {
            var screen = engine.graphics().screen();
            if(engine.keyboard().isDown(Key.ENTER)) {
                screen.drawCircle(screen.center().addX(100), (int) (engine.audio().microphoneLevel().value() * 50), CircleDrawOptions.filled(Color.RED));
                screen.drawCircle(screen.center().addX(100), 50, CircleDrawOptions.outline(Color.WHITE).strokeWidth(2));
            }
        });

        screwBox.start();
    }
}
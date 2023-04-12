package io.github.srcimon.screwbox.examples.helloworld;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.entities.systems.QuitOnKeyPressSystem;
import io.github.srcimon.screwbox.core.graphics.MouseCursor;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.examples.helloworld.systems.PrintHelloWorldSystem;

public class HelloWorldExample {

    public static void main(String[] args) {
        // create the engine
        Engine engine = ScrewBox.createEngine("Hello World Example");

        // hide cursor in window mode
        engine.window().setWindowCursor(MouseCursor.HIDDEN);

        // add systems to the game loop
        engine.entities()
                .add(new PrintHelloWorldSystem()) // print hello world next to mouse position
                .add(new QuitOnKeyPressSystem(Key.ESCAPE)); // quits the engine on pressing ESC

        engine.start(); // start the previously configured engine
    }
}
